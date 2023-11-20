package com.neko.txbot.plugin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.core.Bot;
import com.neko.txbot.core.BotPlugin;
import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.util.MsgUtils;
import com.neko.txbot.util.OkHttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Order(2)
@Slf4j
@Component
@RequiredArgsConstructor
public class DailyPlugin extends BotPlugin {
    private static final String CMD = "/日历";
    private static final String CMD2 = "/日报";

    private final ExpiringMap<String, String> expiringMap = ExpiringMap.builder()
            //允许更新过期时间值,如果不设置variableExpiration，不允许后面更改过期时间,一旦执行更改过期时间操作会抛异常UnsupportedOperationException
            .variableExpiration()
//            1）ExpirationPolicy.ACCESSED ：每进行一次访问，过期时间就会重新计算；
//            2）ExpirationPolicy.CREATED：在过期时间内重新 put 值的话，过期时间重新计算；
            .expirationPolicy(ExpirationPolicy.CREATED)
            //设置每个key有效时间30m,如果key不设置过期时间，key永久有效
            .expiration(30L, TimeUnit.MINUTES)
            .build();

    @Override
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            MsgUtils msg = getCmdMsg();
            bot.sendChannelMsg(event.getChannelId(), event.getId(), msg.build(), msg.buildImg(true));
            return MESSAGE_BLOCK;
        } else if (event.getNoAtContent().startsWith(CMD2)) {
            MsgUtils msg = getCmd2Msg();
            bot.sendChannelMsg(event.getChannelId(), event.getId(), msg.build(), msg.buildImg(true));
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (event.getNoAtContent().startsWith(CMD)) {
            MsgUtils msg = getCmdMsg();
            bot.sendGroupMsgImg(event.getGroupId(), msg.getUrl());
            return MESSAGE_BLOCK;
        } else if (event.getNoAtContent().startsWith(CMD2)) {
            MsgUtils msgUtils = getCmd2Msg();
            if (msgUtils.isImg()) {
                bot.sendGroupMsgImg(event.getGroupId(), msgUtils.getUrl());
                return MESSAGE_BLOCK;
            }
            bot.sendGroupMsg(event.getGroupId(), event.getId(), msgUtils.build());
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }

    private MsgUtils getCmdMsg() {
        MsgUtils msgUtils = MsgUtils.builder();
        // 缓存里加载
        String url = expiringMap.get(CMD);
        if (StringUtils.hasText(url)) {
            msgUtils.img(url);
            return msgUtils;
        }
        // 网络请求
        String string = OkHttpUtil.get("https://api.j4u.ink/v1/store/other/proxy/remote/moyu.json");
        JSONObject jsonObject = JSON.parseObject(string);
        if (jsonObject.getIntValue("code") != 200) {
            msgUtils.text("日报获取失败！");
            return msgUtils;
        }
        String imgUrl = jsonObject.getJSONObject("data").getString("moyu_url");
        String fImgUrl = OkHttpUtil.getRedirectUrl(imgUrl);
        expiringMap.put(CMD, fImgUrl);
        msgUtils.img(fImgUrl);
        return msgUtils;
    }

    private MsgUtils getCmd2Msg() {
        MsgUtils msgUtils = MsgUtils.builder();
        // 缓存里加载
        String url = expiringMap.get(CMD2);
        if (StringUtils.hasText(url)) {
            msgUtils.img(url);
            return msgUtils;
        }
        // 网络请求
        String string = OkHttpUtil.get("https://v2.alapi.cn/api/zaobao?format=json&token=eCKR3lL7uFtt9PIm");
        JSONObject jsonObject = JSON.parseObject(string);
        if (jsonObject.getIntValue("code") != 200) {
            msgUtils.text("日报获取失败！");
            return msgUtils;
        }
        String imgUrl = jsonObject.getJSONObject("data").getString("image");
        expiringMap.put(CMD2, imgUrl);
        msgUtils.img(imgUrl);
        return msgUtils;
    }
}

