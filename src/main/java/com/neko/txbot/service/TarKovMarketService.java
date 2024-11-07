package com.neko.txbot.service;


import com.alibaba.fastjson2.JSON;
import com.neko.txbot.model.TarKovMarketVo;
import com.neko.txbot.model.TarKovRVo;
import com.neko.txbot.util.OkHttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarKovMarketService {
    public static final String TAR_KOV_MARKET_URL = "https://api.tarkov-market.app/api/items?lang=cn&search=%s&tag=&sort=name&sort_direction=desc&trader=&skip=0&limit=2";
//    public static final String TAR_KOV_MARKET_URL = "https://tarkov-market.com/api/be/items?lang=cn&search=%s&tag=&sort=name&sort_direction=desc&trader=&skip=0&limit=2";

    public Optional<List<TarKovMarketVo>> search(String text) {
        OkHttpClient okHttpClient = OkHttpUtil.getOkHttpClient();
        Headers headers = new Headers.Builder()
                .add("Cookie", "cf_clearance=CVzPv4LT32X5WSW0QGJQXrdYI.rT.lo5y4rgxJ1Xi1U-1679205658-0-160; __cf_bm=kjMixk_YXW3S_0NXmwKAwi3lcQ0U_TLOFksVyKb1X6o-1679205665-0-AU8HOoHP/GzAjyCTvPIo8I+x8wAyUrrD8P5CYU4bp7HfQIiBK8Lp8yl+yv4rmp6XmkOcK3riU9NiOK8y2Qhh6wMDfbug2zgwo7k26xbv2uVd8liw4/TqQloFlaCuob+jg7PnMjiVWym1DO16vJWTEk3cKas8M7b34qQvljnHWiJk; tm_locale=cn; HCLBSTICKY=9002f577e4f8d278d30e4c50321e2c20|ZBalV|ZBalH")
                .add("referer", "https://tarkov-market.com/")
                .add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .build();
        String res = OkHttpUtil.get(okHttpClient, TAR_KOV_MARKET_URL.formatted(text), headers);
        if (res.isBlank()) {
            return Optional.empty();
        }
        TarKovRVo tarKovRVo = JSON.parseObject(res, TarKovRVo.class);

        //解密
        String items = tarKovRVo.getItems();
        if (items.length() < 18) {
            return Optional.empty();
        }
        String s1 = items.substring(0, 5);
        String s2 = items.substring(10);
        String urlJson = new String(Base64.getDecoder().decode(s1 + s2));
        String json = URLDecoder.decode(urlJson, StandardCharsets.UTF_8);
        List<TarKovMarketVo> tarKovMarketVos = JSON.parseArray(json, TarKovMarketVo.class);
        return Optional.of(tarKovMarketVos);
    }
}
