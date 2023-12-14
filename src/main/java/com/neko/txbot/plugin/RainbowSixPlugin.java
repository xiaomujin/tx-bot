package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.core.msg.BaseMsg;
import com.neko.txbot.core.msg.ImgMsg;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.menu.Regex;
import com.neko.txbot.menu.SysGlobal;
import com.neko.txbot.util.PuppeteerUtil;
import com.neko.txbot.util.RegexUtil;
import com.ruiyun.jvppeteer.core.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RainbowSixPlugin extends BotPlugin {
    private static final String CMD = Regex.RAINBOW_KD;

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        RegexUtil.matcher(event.getNoAtContent(), CMD).ifPresent(m -> {
            log.info("{}({}) 请求 {}", event.getAuthor().getUsername(), event.getAuthor().getId(), CMD);
            String name = m.group("name").trim();
            BaseMsg msg = getMsg(name);
            bot.sendGroupMsg(event.getGroupId(), event.getId(), msg);
        });
        return MESSAGE_IGNORE;
    }

    @Override
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        RegexUtil.matcher(event.getNoAtContent(), CMD).ifPresent(m -> {
            log.info("{}({}) 请求 {}", event.getAuthor().getUsername(), event.getAuthor().getId(), CMD);
            String name = m.group("name").trim();
            BaseMsg msg = getMsg(name);
            bot.sendChannelMsg(event.getChannelId(), event.getId(), msg);
        });
        return MESSAGE_IGNORE;
    }

    private BaseMsg getMsg(String name) {
        ImgMsg msg = ImgMsg.builder();
        Page page = PuppeteerUtil.getNewPage("https://r6.tracker.network/profile/pc/" + name, "domcontentloaded", 30000, 1920, 1080);
        String img = "r6/" + name + ".png";
        String imgPath = SysGlobal.BASE_IMG_PATH + img;
        PuppeteerUtil.screenshot(page, imgPath,
                "#profile > div.trn-scont.trn-scont--swap > div.trn-scont__content",
                "#trn-site-nav {display:none} .trn-gamebar{display:none}"
        );
        msg.img(SysGlobal.BASE_IMG_URL + img);
        return msg;
    }

}
