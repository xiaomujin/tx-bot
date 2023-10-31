package com.neko.txbot.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.config.BotConfig;
import com.neko.txbot.core.Bot;
import com.neko.txbot.menu.OpCode;
import com.neko.txbot.model.TxPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableAsync
public class BotAsyncTask {
    private final static String OP = "op";

    @Async("botTaskExecutor")
    public void execHandlerMsg(Bot bot, JSONObject payload) throws IOException {
        String t = payload.getString("t");
        switch (t) {
            case "AT_MESSAGE_CREATE" -> {
                String channelId = payload.getJSONObject("d").getString("channel_id");
                String msgId = payload.getJSONObject("d").getString("id");
                bot.sendChannelMsg(channelId, msgId);
            }
        }
    }
}
