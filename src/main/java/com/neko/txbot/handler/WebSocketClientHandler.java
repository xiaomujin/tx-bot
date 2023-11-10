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

@Slf4j
@RequiredArgsConstructor
public class WebSocketClientHandler extends TextWebSocketHandler {
    private final BotAsyncTask botAsyncTask;
    private final Bot bot;

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) {
        JSONObject payload = JSON.parseObject(message.getPayload());
        log.debug("<===== {}", payload.toString());
        Long s = payload.getLong("s");
        bot.setS(s);
        switch (payload.getIntValue("op", OpCode.ERROR)) {
            case OpCode.ERROR -> log.error("返回数据错误");
            case OpCode.DISPATCH -> {
                //分发执行收到的消息
                botAsyncTask.execHandlerMsg(bot, payload);
            }
            case OpCode.HELLO -> {
                JSONObject d = payload.getJSONObject("d");
                int time = d.getIntValue("heartbeat_interval");
                bot.startHeart(time);
                if (bot.isReconnect()) {
                    bot.sendReconnect();
                } else {
                    bot.sendIdentify();
                }
            }
            case OpCode.RECONNECT -> {
                log.warn("重新连接");
                bot.managerRestart(true);
            }
            case OpCode.INVALID_SESSION -> log.error("参数错误:{}", payload);
            case OpCode.HEARTBEAT_ACK -> {
            }
            default -> log.warn("未知的opCode");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.warn("afterConnectionClosed {}---{}", session.getId(), status.toString());
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        bot.setSession(session);
        log.debug("afterConnectionEstablished {}", session.getId());
    }


}
