package com.neko.txbot.timer;


import com.neko.txbot.handler.WSCHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class BotTask {
    private final WSCHandler wscHandler;

    @Scheduled(cron = "0 * * * * ? ")
    public void doDaily() {
        wscHandler.refreshAccessToken();
    }
}
