package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Component
public class LogPlugin extends BotPlugin {
    @Override
    public int onGuildChannelMessage(Bot bot, MessageEvent event) {
        log.info("收到频道:{} 内:{}({}):{} ({})", event.getGuildId(), event.getAuthor().getUsername(), event.getAuthor().getId(), event.getContent(), event.getSeqInChannel());
        return MESSAGE_IGNORE;
    }
}

