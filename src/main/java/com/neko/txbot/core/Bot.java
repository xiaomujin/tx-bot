package com.neko.txbot.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.config.BotConfig;
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
        payload.put("intents", 1 << 30 | 1 << 18 | 1 << 12 | 1 << 10 | 1 << 1 | 1);
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

    private Headers getHeaders1() {
        return new Headers.Builder()
//                .add("Authorization", "QQBot " + botConfig.getAccessToken())
                .add("Authorization", "Bot " + botConfig.getAppId() + "." + botConfig.getClientToken())
                .add("X-Union-Appid", botConfig.getAppId())
                .build();
    }

    public String httpGet(String url) {
        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        return OkHttpUtil.get(okHttpClient, url, getHeaders());
    }

    public String httpPost(String url, JSONObject bodyJson) {
        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        return OkHttpUtil.post(okHttpClient, url, bodyJson, getHeaders1());
    }

    public String sendChannelMsg(String channelId, String atMsgId, String content) {
        return sendChannelMsg(channelId, atMsgId, content, null);
    }

    public String sendChannelMsg(String channelId, String atMsgId, String content, String imgUrl) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.hasText(atMsgId)) {
            jsonObject.put("msg_id", atMsgId);
            JSONObject messageReference = new JSONObject();
            jsonObject.put("message_reference", messageReference);
            messageReference.put("message_id", atMsgId);
            messageReference.put("ignore_get_message_error", true);
        }
        if (StringUtils.hasText(imgUrl)) {
            jsonObject.put("image", imgUrl);
        }
        jsonObject.put("content", content);
        String url = TxApi.TEXT_SUB_CHANNEL.replace("{channel_id}", channelId);
        return httpPost(url, jsonObject);
    }
}
