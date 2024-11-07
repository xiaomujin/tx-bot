package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.core.msg.TextMsg;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.menu.Regex;
import com.neko.txbot.util.RegexUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Order(2)
@Slf4j
@Component
@RequiredArgsConstructor
public class TKFPlugin extends BotPlugin {
    private static final String CMD = Regex.TKF_THREE_DOG;

    private final ExpiringMap<String, TextMsg> expiringMap = ExpiringMap.builder()
            .variableExpiration()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(8L, TimeUnit.MINUTES)
            .build();

    @Override
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        if (RegexUtil.matcher(event.getNoAtContent(), CMD).isPresent()) {
            bot.sendChannelMsg(event.getChannelId(), event.getId(), getCmdMsg());
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (RegexUtil.matcher(event.getNoAtContent(), CMD).isPresent()) {
            bot.sendGroupMsg(event.getGroupId(), event.getId(), getCmdMsg());
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    @SneakyThrows
    private TextMsg getCmdMsg() {
        TextMsg textMsg = expiringMap.get(CMD);
        if (textMsg != null) {
            return textMsg;
        }
        Document document = Jsoup.connect("https://9d33d34f.goon-98b.pages.dev/proxy/https://docs.google.com/spreadsheets/d/e/2PACX-1vR-wIQI351UH85ILq5KiCLMMrl0uHRmjDinBCt6nXGg5exeuCxQUf8DTLJkwn7Ckr8-HmLyEIoapBE5/pubhtml/sheet?headers=false&gid=1420050773").get();
        Elements td = document.getElementsByTag("td");
        String mapName = td.get(2).text();
        String time = td.get(3).text();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy H:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("EST"));
        Date after = df.parse(time);
        df.applyPattern("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getDefault());
        String format = df.format(after);
        textMsg = TextMsg.builder().text("\n" + mapName + "\n").text(format);
        expiringMap.put(CMD, textMsg);
        return textMsg;
    }

}

