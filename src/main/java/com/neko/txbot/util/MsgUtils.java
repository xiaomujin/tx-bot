package com.neko.txbot.util;

/**
 * 消息构建工具
 */
public class MsgUtils {

    private final StringBuilder builder = new StringBuilder();

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
     * 构建消息链
     *
     * @return {@link String}
     */
    public String build() {
        return builder.toString();
    }

}

