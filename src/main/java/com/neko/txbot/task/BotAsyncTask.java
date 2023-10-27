package com.neko.txbot.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.config.BotConfig;
import com.neko.txbot.model.TxPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableAsync
public class BotAsyncTask {
    private final BotConfig botConfig;
    private final static String OP = "op";

    @Async("botTaskExecutor")
    public void execHandlerMsg(WebSocketSession session, JSONObject payload) throws IOException {
        log.info(payload.toString());
        if (payload.getInteger(OP).equals(10)) {
            JSONObject d = new JSONObject();
//            d.put("token", "Bot " + botConfig.getAppId() + "." + botConfig.getClientToken());
            d.put("token", "QQBot " + botConfig.getAccessToken());
            d.put("intents", 1 << 30);
            d.put("shard", List.of(0, 1));
            JSONObject properties = new JSONObject();
            d.put("properties", properties);
            properties.put("$os", "linux");
            properties.put("$browser", "Neko_browser");
            properties.put("$device", "Neko_device");
            TxPayload sendPayload = new TxPayload(2, d, null, null);
            String jsonString = JSON.toJSONString(sendPayload);
            log.warn(jsonString);
            session.sendMessage(new TextMessage(JSON.toJSONBytes(sendPayload)));
        }
    }
}
