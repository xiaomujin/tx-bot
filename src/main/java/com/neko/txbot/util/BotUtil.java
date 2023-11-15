package com.neko.txbot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class BotUtil {

    public static Optional<String> getOneParam(String cmd, String msg) {
        List<String> params = getParams(cmd, msg, 1);
        if (params.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(params.get(0));
    }

    public static List<String> getParams(String cmd, String msg, int paramNum) {
        if (!StringUtils.hasText(msg)) {
            return List.of();
        }
        ArrayList<String> list = new ArrayList<>();
        if (msg.trim().length() > cmd.length()) {
            String mainMsg = msg.substring(cmd.length()).trim();
            String[] split = mainMsg.split("\\s+", paramNum);
            list.addAll(Arrays.asList(split));
        }
        log.info("{}:getParams:{}", cmd, list);
        return list;
    }

    public static List<String> getParams(String cmd, String msg) {
        return getParams(cmd, msg, 0);
    }
}
