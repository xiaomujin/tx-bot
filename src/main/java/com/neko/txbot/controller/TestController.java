package com.neko.txbot.controller;

import com.neko.txbot.model.TarKovMarketVo;
import com.neko.txbot.service.TarKovMarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TarKovMarketService tarKovMarketService;

    @RequestMapping(value = "/testTarKovMarket")
    public Optional<List<TarKovMarketVo>> testTarKovMarket() {
        return tarKovMarketService.search("btc");
    }
}
