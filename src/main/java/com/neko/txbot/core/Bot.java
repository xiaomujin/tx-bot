package com.neko.txbot.core;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.config.BotConfig;
import com.neko.txbot.menu.OpCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Slf4j
@Data
public class Bot {
    private Timer heartTimer;
    private WebSocketSession session;
    private Long s;
    private BotConfig botConfig;
    private String sessionId;
    private String userId;

    private final static String OP = "op";
    private final static String D = "d";

    public void startHeart(int time) {
        if (heartTimer != null) {
            heartTimer.cancel();
        } else {
            heartTimer = new Timer("bot-heart-0");
        }
        heartTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                send(OpCode.HEARTBEAT);
            }
        }, 2000, time);
    }

    public void send(int op) {
        JSONObject payload = new JSONObject();
        payload.put(OP, op);
        payload.put(D, s);
        log.info("=====> {}", payload);
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
}
