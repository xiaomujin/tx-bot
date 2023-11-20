package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.dto.event.message.GuildMemberAddEvent;
import com.neko.txbot.dto.event.message.GuildMemberRemoveEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Component
public class LogPlugin extends BotPlugin {
    @Override
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        log.info("收到频道:{} 内:{}({}):{} ({})", event.getGuildId(), event.getAuthor().getUsername(), event.getAuthor().getId(), event.getContent(), event.getSeqInChannel());
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        log.info("收到群:{} 内:({}):{}", event.getGroupId(), event.getAuthor().getId(), event.getContent());
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGuildMemberAdd(Bot bot, GuildMemberAddEvent event) {
        log.info("退出频道:{} 内:{}({})", event.getGuildId(), event.getUser().getUsername(), event.getUser().getId());
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGuildMemberRemove(Bot bot, GuildMemberRemoveEvent event) {
        log.info("加入频道:{} 内:{}({})", event.getGuildId(), event.getUser().getUsername(), event.getUser().getId());
        return MESSAGE_IGNORE;
    }
}

