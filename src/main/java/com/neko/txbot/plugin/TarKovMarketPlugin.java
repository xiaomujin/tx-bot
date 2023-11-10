package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.dto.event.message.MessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Slf4j
@Component
@RequiredArgsConstructor
public class TarKovMarketPlugin extends BotPlugin {
    @Override
    public int onGuildChannelMessage(Bot bot, MessageEvent event) {
        log.info("TarKovMarketPlugin");
        String content = event.getContent();
        content = content.replaceAll("<@!.*?>", "").trim();
        String res = bot.sendChannelMsg(event.getChannelId(), event.getId(), content);
        return MESSAGE_IGNORE;
    }
}

