package com.neko.txbot.task;

import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.core.Bot;
import com.neko.txbot.dto.event.message.MessageEvent;
import com.neko.txbot.dto.event.message.ReadyEvent;
import com.neko.txbot.dto.event.message.TxPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableAsync
public class BotAsyncTask {

    @Async("botTaskExecutor")
    public void execHandlerMsg(Bot bot, JSONObject payload) {
        TxPayload txPayload = payload.to(TxPayload.class);
        switch (txPayload.getT()) {
            case "READY" -> {
                ReadyEvent readyEvent = txPayload.getD().to(ReadyEvent.class);
                bot.setSessionId(readyEvent.getSessionId());
                bot.setUserId(readyEvent.getUser().getId());
            }
            case "AT_MESSAGE_CREATE" -> {
                MessageEvent messageEvent = txPayload.getD().to(MessageEvent.class);
                String channelId = messageEvent.getChannelId();
                String msgId = messageEvent.getId();
                String content = messageEvent.getContent();
                content = content.replaceAll("<@!.*?>", "").trim();
                String res = bot.sendChannelMsg(channelId, msgId, content);
            }
        }
    }
}
