package com.neko.txbot.config;

import com.neko.txbot.menu.Intent;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "bot")
@Configuration
@Data
public class BotConfig {
    private String appId;
    private String clientToken;
    private String clientSecret;
    private String accessToken;
    private List<Intent> intents = new ArrayList<>();
}

