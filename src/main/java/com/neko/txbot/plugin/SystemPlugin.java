package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.model.TarKovMarketVo;
import com.neko.txbot.service.TarKovMarketService;
import com.neko.txbot.util.BotUtil;
import com.neko.txbot.util.MsgUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Order(1)
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemPlugin extends BotPlugin {
    private static final String CMD = "/版本更新";

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            Optional<String> oneParam = BotUtil.getOneParam(CMD, event.getNoAtContent());
            oneParam.filter(it -> it.equals("114514")).ifPresent(it -> {
                MsgUtils msg = MsgUtils.builder();
                msg.text("开始更新，预计需要3分钟！");
                bot.sendGroupMsg(event.getGroupId(), event.getId(), msg.build(), 1);
                ProcessBuilder pb = new ProcessBuilder("sh", "/mnt/tx-qqbot/release.sh");
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
                try {
                    pb.start();
                } catch (IOException e) {
                    bot.sendGroupMsg(event.getGroupId(), event.getId(), MsgUtils.builder().text(e.getMessage()).build(), 2);
                    log.error("重启失败", e);
                }
            });
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

}

