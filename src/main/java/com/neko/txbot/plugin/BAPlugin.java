package com.neko.txbot.plugin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.core.msg.BaseMsg;
import com.neko.txbot.core.msg.TextMsg;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.exception.BotException;
import com.neko.txbot.model.BaRankInfo;
import com.neko.txbot.util.MsgUtils;
import com.neko.txbot.util.OkHttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Order(2)
@Slf4j
@Component
@RequiredArgsConstructor
public class BAPlugin extends BotPlugin {
    private static final String CMD = "/总力战";

    private final ExpiringMap<String, ArrayList<BaseMsg>> expiringMap = ExpiringMap.builder()
            //允许更新过期时间值,如果不设置variableExpiration，不允许后面更改过期时间,一旦执行更改过期时间操作会抛异常UnsupportedOperationException
            .variableExpiration()
//            1）ExpirationPolicy.ACCESSED ：每进行一次访问，过期时间就会重新计算；
//            2）ExpirationPolicy.CREATED：在过期时间内重新 put 值的话，过期时间重新计算；
            .expirationPolicy(ExpirationPolicy.CREATED)
            //设置每个key有效时间5m,如果key不设置过期时间，key永久有效
            .expiration(5L, TimeUnit.MINUTES)
            .build();

    @Override
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            ArrayList<BaseMsg> msgList = getCmdMsg();
            bot.sendChannelMsg(event.getChannelId(), event.getId(), msgList);
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            ArrayList<BaseMsg> msgList = getCmdMsg();
            bot.sendGroupMsg(event.getGroupId(), event.getId(), msgList);
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    private ArrayList<BaseMsg> getCmdMsg() {
        ArrayList<BaseMsg> arrayList = expiringMap.get(CMD);
        if (arrayList != null) {
            return arrayList;
        }

        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        Headers header = new Headers.Builder().add("Authorization", "ba-token uuz:uuz").build();

        String seasonListStr = OkHttpUtil.get(okHttpClient, "https://api.arona.icu/api/season/list", header);
        if (!StringUtils.hasText(seasonListStr)) {
            throw new BotException("获取总力战数据失败！");
        }
        JSONObject jsonObject = JSON.parseObject(seasonListStr);
        JSONArray data = jsonObject.getJSONArray("data");
        JSONObject seasonInfo = data.getJSONObject(0);
        Integer season = seasonInfo.getInteger("season");

        String rankListStr = OkHttpUtil.get(okHttpClient, "https://api.arona.icu/api/rank/list/1/2/" + season + "?page=1&size=50", header);
        if (!StringUtils.hasText(rankListStr)) {
            throw new BotException("获取总力战数据失败！");
        }
        JSONObject rankListJO = JSON.parseObject(rankListStr);
        JSONArray rankListData = rankListJO.getJSONObject("data").getJSONArray("records");
        List<BaRankInfo> javaList = rankListData.toJavaList(BaRankInfo.class);
//            ResponseEntity<String> listByLastRank = restTemplate.exchange("https://api.arona.icu/api/rank/list_by_last_rank?server=1&season=" + season, HttpMethod.GET, httpEntity, String.class);
//            if (!rankList.getStatusCode().is2xxSuccessful()) {
//                throw new BotException("获取总力战数据失败！");
//            }
//            JSONObject listByLastRankJO = JSON.parseObject(listByLastRank.getBody());
//            JSONArray listByLastRankData = listByLastRankJO.getJSONArray("data");
        ArrayList<BaseMsg> list = new ArrayList<>();
        TextMsg msg = TextMsg.builder();
        String mapName = seasonInfo.getJSONObject("map").getString("value");
        msg.text("\n第").text(season.toString()).text("期 ").text(mapName).text(seasonInfo.getString("boss"));
        msg.text("\n开始：").text(seasonInfo.getString("startTime"));
        msg.text("\n结束：").text(seasonInfo.getString("endTime"));
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now());
        msg.text("\n数据更新于：\n");
        msg.text(format);
        msg.text("\n--------------------\n");
        msg.text("排名  |    分数    | 难度\n");
        for (int i = 6; i < javaList.size() + 6; i++) {
            BaRankInfo rankInfo = javaList.get(i - 6);
            msg.text(formatRank(rankInfo));
            if (i % 13 == 0) {
                list.add(msg);
                msg = TextMsg.builder();
            }
        }
        if (StringUtils.hasText(msg.build())) {
            list.add(msg);
        }
        expiringMap.put(CMD, list);
        return list;
    }

    private String formatRank(BaRankInfo rankInfo) {
        return "%-6d %-10d %-2s\n".formatted(rankInfo.getRank(), rankInfo.getBestRankingPoint(), rankInfo.getHard());
    }

}

