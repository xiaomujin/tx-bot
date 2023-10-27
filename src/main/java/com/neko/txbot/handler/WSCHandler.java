package com.neko.txbot.handler;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.config.BotConfig;
import com.neko.txbot.core.Bot;
import com.neko.txbot.menu.TxApi;
import com.neko.txbot.task.BotAsyncTask;
import com.neko.txbot.util.OkHttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class WSCHandler implements ApplicationRunner {

    private final BotConfig botConfig;
    private final Bot bot;
    private final BotAsyncTask botAsyncTask;

    private long expiresOn = 0L;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Start Link!");
        refreshAccessToken();
        String websocketUrl = getWebsocketUrl();
        StandardWebSocketClient client = new StandardWebSocketClient();
        bot.setBotConfig(botConfig);
        WebSocketClientHandler handler = new WebSocketClientHandler(botAsyncTask, bot);
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client, handler, URI.create(websocketUrl));
        manager.start();
    }

    private String getWebsocketUrl() {
        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        Headers headers = new Headers.Builder()
                .add("Authorization", "QQBot " + botConfig.getAccessToken())
                .add("X-Union-Appid", botConfig.getAppId())
                .build();
        String get = OkHttpUtil.get(okHttpClient, TxApi.GATEWAY, headers);
        JSONObject res = JSON.parseObject(get);
        return res.getString("url");
    }

    public void refreshAccessToken() {
        if (expiresOn - Instant.now().getEpochSecond() > 60) {
            return;
        }
        log.info("RefreshAccessToken Start");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId", botConfig.getAppId());
        jsonObject.put("clientSecret", botConfig.getClientSecret());
        String post = OkHttpUtil.post(TxApi.GET_APP_ACCESS_TOKEN, jsonObject);
        JSONObject res = JSON.parseObject(post);
        String accessToken = res.getString("access_token");
        long expiresIn = res.getLongValue("expires_in");
        botConfig.setAccessToken(accessToken);
        expiresOn = Instant.now().getEpochSecond() + expiresIn;
        log.info("RefreshAccessToken End");
    }
}
