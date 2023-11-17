package com.neko.txbot.util;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息构建工具
 */
public class MsgUtils {

    private final StringBuilder builder = new StringBuilder();
    @Getter
    private String url = "";

    @Getter
    private boolean isImg = false;
    public static final String PROXY_IMG_URL = "https://i3.wp.com/";

    /**
     * 消息构建
     *
     * @return {@link MsgUtils}
     */
    public static MsgUtils builder() {
        return new MsgUtils();
    }

    /**
     * 文本内容
     *
     * @param text 内容
     * @return {@link MsgUtils}
     */
    public MsgUtils text(String text) {
        builder.append(text);
        return this;
    }

    /**
     * 文本内容
     *
     * @param url 链接地址
     * @return {@link MsgUtils}
     */
    public MsgUtils img(String url) {
        this.url = url;
        this.isImg = true;
        return this;
    }

    /**
     * 构建消息链
     *
     * @return {@link String}
     */
    public String build() {
        return builder.toString();
    }

    /**
     * 构建消息链
     *
     * @return {@link String}
     */
    public String buildImg(boolean proxy) {
        if (isImg) {
            if (proxy) {
                url = url.replaceFirst("^https?://", "");
                url = PROXY_IMG_URL + url;
            }
            return url;
        }
        return "";
    }

}

