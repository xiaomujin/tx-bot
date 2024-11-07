package com.neko.txbot.plugin;

import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.core.msg.BaseMsg;
import com.neko.txbot.core.msg.ImgMsg;
import com.neko.txbot.core.msg.TextMsg;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.model.TarKovMarketVo;
import com.neko.txbot.service.TarKovMarketService;
import com.neko.txbot.util.BotUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Order(1)
@Slf4j
@Component
@RequiredArgsConstructor
public class TarKovMarketPlugin extends BotPlugin {
    private final TarKovMarketService tarKovMarketService;
    private static final String CMD = "跳蚤";

    @Override
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            ArrayList<BaseMsg> msg = getMsg(event.getNoAtContent());
            bot.sendChannelMsg(event.getChannelId(), event.getId(), msg);
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            ArrayList<BaseMsg> msg = getMsg(event.getNoAtContent());
            bot.sendGroupMsg(event.getGroupId(), event.getId(), msg);
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    private ArrayList<BaseMsg> getMsg(String content) {
        ArrayList<BaseMsg> msgList = new ArrayList<>();
        Optional<String> oneParam = BotUtil.getOneParam(CMD, content);
        Optional<List<TarKovMarketVo>> list = oneParam.flatMap(tarKovMarketService::search);
        list.ifPresent(tarKovMarketVos -> tarKovMarketVos.forEach(it -> {
            TextMsg msg = TextMsg.builder();
            ImgMsg img = ImgMsg.builder().img(it.getEnImg());
            msgList.add(img);
            msg.text("\n名称：").text(it.getCnName() + "\n");
            String format = it.getUpdated().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            msg.text("时间：").text(format + "\n");
            msg.text("24h：").text(it.getChange24() + "%").text("  7d：").text(it.getChange7d() + "%\n");
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
            msgList.add(msg);
        }));
        if (msgList.isEmpty()) {
            TextMsg text = TextMsg.builder().text("没有找到：").text(oneParam.orElse(""));
            msgList.add(text);
        }
        return msgList;
    }
}

