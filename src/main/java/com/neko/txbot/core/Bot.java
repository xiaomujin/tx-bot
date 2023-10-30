package com.neko.txbot.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.config.BotConfig;
import com.neko.txbot.menu.OpCode;
import com.neko.txbot.model.TxPayload;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Slf4j
@Data
public class Bot {
    private Timer heartTimer;
    private WebSocketSession session;
    private WebSocketConnectionManager manager;
    private Long s;
    private BotConfig botConfig;
    private String sessionId;
    private String userId;
    private boolean isReconnect = false;

    private final static String OP = "op";
    private final static String D = "d";

    public void startHeart(int time) {
        if (heartTimer != null) {
            heartTimer.cancel();
        }
        heartTimer = new Timer("bot-heart-0");
        heartTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendHeart();
            }
        }, 2000, time);
    }

    private void sendHeart() {
        send(OpCode.HEARTBEAT, s);
    }

    public void sendIdentify() {
        JSONObject payload = new JSONObject();
        payload.put("token", getToken());
        payload.put("intents", 1 << 30 | 1 << 18 | 1 << 12 | 1 << 10 | 1 << 1 | 1 << 0);
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
        payload.put("token", "QQBot " + getToken());
        payload.put("session_id", sessionId);
        payload.put("seq", s);
        send(OpCode.RESUME, payload);
    }

    public void send(int op, Object d) {
        JSONObject payload = new JSONObject();
        payload.put(OP, op);
        payload.put(D, d);
        log.info("=====> {}", payload.toJSONString());
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
}
