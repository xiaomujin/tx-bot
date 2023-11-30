package com.neko.txbot.util;

import com.neko.txbot.menu.Regex;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    private static final Map<String, Pattern> cache = new ConcurrentHashMap<>();

    private static final String REGEX_NUM = "\\d+";

    public static void main(String[] args) {
        Map<String, Pattern> cache = new ConcurrentHashMap<>();
        String input = "https://www.bilibili.com/video/BV17D4y1z7tW?";
        Pattern pattern = cache.computeIfAbsent(Regex.BILIBILI_BID, Pattern::compile);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String bid = matcher.group("bid");
            System.out.println("Bid: " + bid);
        } else {
            System.out.println("No match found.");
        }
    }

    public static Optional<Matcher> matcher(String text, String regex) {
        Pattern pattern = cache.computeIfAbsent(regex, Pattern::compile);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches() ? Optional.of(matcher) : Optional.empty();
    }

    public static boolean isNumber(String text) {
        return Pattern.matches(REGEX_NUM, text);
    }

    public static Optional<String> group(String group, String text, String regex) {
        Optional<Matcher> match = matcher(regex, text);
        return match.map(v -> v.group(group));
    }

    public static Optional<String> group(int group, String text, String regex) {
        Optional<Matcher> match = matcher(regex, text);
        return match.map(v -> v.group(group));
    }

    public static boolean matches(String text, String regex) {
        return text.matches(regex);
    }

}
