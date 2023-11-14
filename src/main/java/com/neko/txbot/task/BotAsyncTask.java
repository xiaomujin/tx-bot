package com.neko.txbot.task;

import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.dto.event.message.*;
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
                ChannelMessageEvent messageEvent = txPayload.getD().to(ChannelMessageEvent.class);
                String content = messageEvent.getContent();
                messageEvent.setNoAtContent(content.replaceAll("<@!.*?>", "").trim());
                messageEvent.setEventType("CHANNEL");
                for (BotPlugin botPlugin : bot.getBotPlugins()) {
                    if (botPlugin.onChannelMessage(bot, messageEvent) == BotPlugin.MESSAGE_BLOCK) {
                        break;
                    }
                }
            }
            case "GROUP_AT_MESSAGE_CREATE" -> {
                GroupMessageEvent messageEvent = txPayload.getD().to(GroupMessageEvent.class);
                String content = messageEvent.getContent();
                messageEvent.setNoAtContent(content.replaceAll("<@!.*?>", "").trim());
                messageEvent.setEventType("GROUP");
                for (BotPlugin botPlugin : bot.getBotPlugins()) {
                    if (botPlugin.onGroupMessage(bot, messageEvent) == BotPlugin.MESSAGE_BLOCK) {
                        break;
                    }
                }
            }
            default -> log.warn("未知的事件类型: {} : {}", txPayload.getT(), txPayload);
        }
    }
}

