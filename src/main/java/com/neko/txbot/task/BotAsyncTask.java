package com.neko.txbot.task;

import com.alibaba.fastjson2.JSONObject;
import com.neko.txbot.core.Bot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableAsync
public class BotAsyncTask {

    @Async("botTaskExecutor")
    public void execHandlerMsg(Bot bot, JSONObject payload) {
        String t = payload.getString("t");
        switch (t) {
            case "AT_MESSAGE_CREATE" -> {
                String channelId = payload.getJSONObject("d").getString("channel_id");
                String msgId = payload.getJSONObject("d").getString("id");
                String content = payload.getJSONObject("d").getString("content");
                content = content.replaceAll("<@!.*?>", "").trim();
                String res = bot.sendChannelMsg(channelId, msgId, content);
            }
        }
    }
}
