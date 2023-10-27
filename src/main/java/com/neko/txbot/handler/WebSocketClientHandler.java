package com.neko.txbot.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.core.Bot;
import com.neko.txbot.menu.OpCode;
import com.neko.txbot.model.TxPayload;
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
import java.util.List;

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
                }
            }
            case OpCode.HELLO -> {
                JSONObject d = payload.getJSONObject(D);
                int time = d.getIntValue(HEARTBEAT_INTERVAL);
                bot.startHeart(time);
                if (bot.isReconnect()) {
                    JSONObject data = new JSONObject();
//            data.put("token", "Bot " + botConfig.getAppId() + "." + bot.getBotConfig().getClientToken());
                    data.put("token", "QQBot " + bot.getBotConfig().getAccessToken());
                    data.put("session_id", bot.getSessionId());
                    data.put("seq", bot.getS());
                    TxPayload sendPayload = new TxPayload(OpCode.RESUME, data, null, null);
                    session.sendMessage(new TextMessage(JSON.toJSONBytes(sendPayload)));
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
            default -> botAsyncTask.execHandlerMsg(bot, payload);
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
        //鉴权
        if (!bot.isReconnect()) {
            JSONObject d = new JSONObject();
//            d.put("token", "Bot " + botConfig.getAppId() + "." + bot.getBotConfig().getClientToken());
            d.put("token", "QQBot " + bot.getBotConfig().getAccessToken());
            d.put("intents", 1 << 30);
            d.put("shard", List.of(0, 1));
            JSONObject properties = new JSONObject();
            d.put("properties", properties);
            properties.put("$os", "linux");
            properties.put("$browser", "Neko_browser");
            properties.put("$device", "Neko_device");
            TxPayload sendPayload = new TxPayload(2, d, null, null);
            session.sendMessage(new TextMessage(JSON.toJSONBytes(sendPayload)));
        }
    }


}
