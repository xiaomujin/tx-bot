package com.neko.txbot.menu;

import com.neko.txbot.core.msg.BaseMsg;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SysGlobal {
    public static final String BASE_IMG_PATH = "/www/wwwroot/ct-game.com/img/";
    public static final String BASE_IMG_URL = "https://ct-game.com/img/";

    /**
     * 全局默认1分钟 缓存消息
     */
    public final ExpiringMap<String, ArrayList<BaseMsg>> expiringMap = ExpiringMap.builder()
            .variableExpiration()
            .expirationPolicy(ExpirationPolicy.CREATED)
            .expiration(1L, TimeUnit.MINUTES)
            .build();
}
