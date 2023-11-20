package com.neko.txbot.exception;

import com.neko.txbot.core.Bot;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

@Slf4j
public class ExceptionHandler {
    public static void with(Bot bot, MessageEvent event, Supplier<String> block) {
        try {
            String msg = block.get();
            if (StringUtils.hasText(msg)) {
                push(event, bot, msg);
            }
        } catch (BotException e) {
            if (StringUtils.hasText(e.getMessage())) {
                push(event, bot, e.getMessage());
            }
        } catch (Exception e) {
            push(event, bot, "ERROR: " + e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    private static void push(MessageEvent event, Bot bot, String message) {
        switch (event) {
            case GroupMessageEvent it -> bot.sendGroupMsg(it.getGroupId(), it.getId(), message);
            case ChannelMessageEvent it -> bot.sendChannelMsg(it.getChannelId(), it.getId(), message);
            default -> {
            }
        }
    }

}
