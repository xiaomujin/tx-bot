package com.neko.txbot.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.core.Bot;
import com.neko.txbot.menu.OpCode;
import com.neko.txbot.task.BotAsyncTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class WebSocketClientHandler extends TextWebSocketHandler {
    private final BotAsyncTask botAsyncTask;
    private final Bot bot;
    private final static String OP = "op";
    private final static String D = "d";
    private final static String S = "s";
    private final static String HEARTBEAT_INTERVAL = "heartbeat_interval";

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws IOException {
        JSONObject payload = JSON.parseObject(message.getPayload());
        log.info("<===== {}", payload.toString());
        Long s = payload.getLong(S);
        bot.setS(s);
        switch (payload.getIntValue(OP, OpCode.ERROR)) {
            case OpCode.ERROR -> log.error("返回数据错误");
            case OpCode.DISPATCH -> {
                JSONObject d = payload.getJSONObject(D);
                if (s.equals(1L)) {
                    bot.setSessionId(d.getString("session_id"));
                    bot.setUserId(d.getJSONObject("user").getString("id"));
                } else {
                    botAsyncTask.execHandlerMsg(bot, payload);
                }
            }
            case OpCode.HELLO -> {
                JSONObject d = payload.getJSONObject(D);
                int time = d.getIntValue(HEARTBEAT_INTERVAL);
                bot.startHeart(time);
                if (bot.isReconnect()) {
                    bot.sendReconnect();
                } else {
                    bot.sendIdentify();
                }
            }
            case OpCode.RECONNECT -> {
                log.warn("重新连接");
                WebSocketConnectionManager manager = bot.getManager();
                manager.stop();
                bot.setReconnect(true);
                manager.start();
            }
            case OpCode.HEARTBEAT_ACK -> {
            }
            default -> {}
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.warn("afterConnectionClosed {}---{}", session.getId(), status.toString());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        log.warn("afterConnectionEstablished {}", session.getId());
        bot.setSession(session);
    }


}
