package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.core.msg.TextMsg;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.util.BotUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Order(1)
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemPlugin extends BotPlugin {
    private static final String CMD = "版本更新";

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            Optional<String> oneParam = BotUtil.getOneParam(CMD, event.getNoAtContent());
            oneParam.filter(it -> it.equals("114514")).ifPresent(it -> {
                TextMsg msg = TextMsg.builder();
                msg.text("开始更新，预计需要3分钟！");
                bot.sendGroupMsg(event.getGroupId(), event.getId(), msg);
                ProcessBuilder pb = new ProcessBuilder("sh", "/mnt/qqbot/tx_bot/release.sh");
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
                try {
                    pb.start();
                } catch (IOException e) {
                    bot.sendGroupMsg(event.getGroupId(), event.getId(), TextMsg.builder().text(e.getMessage()));
                    log.error("重启失败", e);
                }
            });
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

}

