package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.MessageEvent;
import com.neko.txbot.model.TarKovMarketVo;
import com.neko.txbot.service.TarKovMarketService;
import com.neko.txbot.util.MsgUtil;
import com.neko.txbot.util.MsgUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Order(1)
@Slf4j
@Component
@RequiredArgsConstructor
public class TarKovMarketPlugin extends BotPlugin {
    private final TarKovMarketService tarKovMarketService;
    private static final String CMD = "/跳蚤";

    @Override
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            Optional<String> oneParam = MsgUtil.getOneParam(CMD, event.getNoAtContent());
            Optional<List<TarKovMarketVo>> list = oneParam.flatMap(tarKovMarketService::search);
            list.ifPresent(tarKovMarketVos -> {
                tarKovMarketVos.forEach(it -> {
                    MsgUtils msg = MsgUtils.builder();
                    msg.text("名称：").text(it.getCnName() + "\n");
                    msg.text("波动 24h：").text(it.getChange24() + "%").text("  7d：").text(it.getChange7d() + "%\n");
                    msg.text("基础价格：").text(it.getBasePrice() + "₽" + "\n");
                    msg.text(it.getTraderName()).text("：").text(it.getTraderPrice() + it.getTraderPriceCur() + "\n");
                    if (it.isCanSellOnFlea()) {
                        msg.text("跳蚤日价：").text(it.getAvgDayPrice() + "₽" + "\n");
                        msg.text("跳蚤周价：").text(it.getAvgWeekPrice() + "₽" + "\n");
                        msg.text("单格：").text(it.getAvgDayPrice() / it.getSize() + "₽");
                    } else {
                        msg.text("单格：").text(it.getTraderPrice() / it.getSize() + it.getTraderPriceCur() + "\n");
                        msg.text("跳蚤禁售!");
                    }
//                    bot.sendChannelMsg(event.getChannelId(), event.getId(), msg.build(), it.getEnImg());
                    bot.sendChannelMsg(event.getChannelId(), event.getId(), msg.build());
                });
            });
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }
}

