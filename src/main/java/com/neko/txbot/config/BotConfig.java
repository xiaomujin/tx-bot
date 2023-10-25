package com.neko.txbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "bot")
@Configuration
@Data
public class BotConfig {
    private String appId;
    private String clientToken;
    private String clientSecret;
    private String accessToken;
}

