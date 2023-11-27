package com.neko.txbot.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.config.BotConfig;
import com.neko.txbot.core.msg.BaseMsg;
import com.neko.txbot.core.msg.ImgMsg;
import com.neko.txbot.core.msg.TextMsg;
import com.neko.txbot.dto.event.message.MessageReference;
import com.neko.txbot.menu.Intent;
import com.neko.txbot.menu.OpCode;
import com.neko.txbot.menu.TxApi;
import com.neko.txbot.util.OkHttpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
@Data
public class Bot {
    private Timer heartTimer;
    private WebSocketSession session;
    private WebSocketConnectionManager manager;
    private List<BotPlugin> botPlugins;
    private Long s;
    private BotConfig botConfig;
    private String sessionId;
    private String userId;
    private boolean isReconnect = false;

    private ThreadLocal<String> groupMsgId = new ThreadLocal<>();
    private ThreadLocal<Long> groupMsgSeq = new ThreadLocal<>();

    public void startHeart(int time) {
        stopTimer();
        heartTimer = new Timer("bot-heart-0");
        heartTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendHeart();
            }
        }, 2000, time);
    }

    private void sendHeart() {
        try {
            send(OpCode.HEARTBEAT, s);
        } catch (Exception e) {
            stopTimer();
            managerRestart(true);
        }
    }

    private void stopTimer() {
        if (heartTimer != null) {
            heartTimer.cancel();
        }
    }

    public void sendIdentify() {
        JSONObject payload = new JSONObject();
        payload.put("token", getToken());
        long intents = Intent.calcList(botConfig.getIntents());
        payload.put("intents", intents);
        payload.put("shard", List.of(0, 1));
        JSONObject properties = new JSONObject();
        payload.put("properties", properties);
        properties.put("$os", "linux");
        properties.put("$browser", "Neko_browser");
        properties.put("$device", "Neko_device");
        send(OpCode.IDENTIFY, payload);
    }

    public void sendReconnect() {
        JSONObject payload = new JSONObject();
        payload.put("token", getToken());
        payload.put("session_id", sessionId);
        payload.put("seq", s);
        send(OpCode.RESUME, payload);
    }

    public void managerRestart(boolean reconnect) {
        manager.stop();
        setReconnect(reconnect);
        try {
            Thread.sleep(2500L);
        } catch (InterruptedException e) {
            log.error("sleep error", e);
        }
        manager.start();
    }

    public void send(int op, Object d) {
        JSONObject payload = new JSONObject();
        payload.put("op", op);
        payload.put("d", d);
        log.debug("=====> {}", payload.toJSONString());
        try {
            session.sendMessage(new TextMessage(JSON.toJSONBytes(payload)));
        } catch (IOException e) {
            log.error("发送失败", e);
        }
    }

    public void setS(Long s) {
        if (s == null) {
            return;
        }
        this.s = s;
    }

    public String getToken() {
        return "QQBot " + botConfig.getAccessToken();
    }


    private Headers getHeaders() {
        return new Headers.Builder()
                .add("Authorization", getToken())
                .add("X-Union-Appid", botConfig.getAppId())
                .build();
    }

    public String httpGet(String url) {
        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        return OkHttpUtil.get(okHttpClient, url, getHeaders());
    }

    public String httpPost(String url, JSONObject bodyJson) {
        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        return OkHttpUtil.post(okHttpClient, url, bodyJson, getHeaders());
    }

    public void sendChannelMsg(String groupOpenid, String msgId, List<BaseMsg> msgList) {
        msgList.forEach(msg -> sendChannelMsg(groupOpenid, msgId, msg));
    }

    public String sendChannelMsg(String channelId, String msgId, BaseMsg msg) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.hasText(msgId)) {
            jsonObject.put("msg_id", msgId);
            MessageReference messageReference = new MessageReference(msgId, true);
            jsonObject.put("message_reference", messageReference);
        }

        switch (msg) {
            case TextMsg it:
                jsonObject.put("content", it.build());
                break;
            case ImgMsg it:
                jsonObject.put("image", it.build());
                break;
            default:
                log.error("未知消息类型: {}", msg);
        }

        String url = TxApi.SEND_CHANNEL.replace("{channel_id}", channelId);
        return httpPost(url, jsonObject);
    }

    private Long getMsgSeq(String msgId) {
        String localMsgId = groupMsgId.get();
        if (msgId.equals(localMsgId)) {
            Long msgSeq = groupMsgSeq.get();
            if (msgSeq == null) {
                msgSeq = 0L;
            }
            groupMsgSeq.set(++msgSeq);
            return msgSeq;
        } else {
            groupMsgSeq.set(0L);
            groupMsgId.set(msgId);
            return 0L;
        }
    }

    private JSONObject getGroupFileInfo(String groupOpenid, String imgUrl, int fileType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file_type", fileType);
        jsonObject.put("url", imgUrl);
        jsonObject.put("srv_send_msg", false);
        String url = TxApi.SEND_GROUP_FILE.replace("{group_openid}", groupOpenid);
        String res = httpPost(url, jsonObject);
        return JSON.parseObject(res);
    }

    public String sendGroupMsg(String groupOpenid, String msgId, BaseMsg msg) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.hasText(msgId)) {
            jsonObject.put("msg_id", msgId);
        }
        jsonObject.put("content", " ");
        jsonObject.put("msg_seq", getMsgSeq(msgId));
        jsonObject.put("timestamp", Instant.now().getEpochSecond());

        switch (msg) {
            case TextMsg it:
                jsonObject.put("content", it.build());
                jsonObject.put("msg_type", 0);
                break;
            case ImgMsg it:
                jsonObject.put("msg_type", 7);
                JSONObject groupImgInfo = getGroupFileInfo(groupOpenid, it.build(), 1);
                if (groupImgInfo == null) {
                    log.error("发送群图片，获取图片信息失败");
                    return "";
                }
                jsonObject.put("media", groupImgInfo);
                break;
            default:
                log.error("未知消息类型: {}", msg);
        }
        String url = TxApi.SEND_GROUP.replace("{group_openid}", groupOpenid);
        return httpPost(url, jsonObject);
    }

    public void sendGroupMsg(String groupOpenid, String msgId, List<BaseMsg> msgList) {
        msgList.forEach(msg -> sendGroupMsg(groupOpenid, msgId, msg));
    }
}
