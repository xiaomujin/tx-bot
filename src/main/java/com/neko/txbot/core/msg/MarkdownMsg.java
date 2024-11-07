package com.neko.txbot.core.msg;

public class MarkdownMsg extends BaseMsg {
    private final StringBuilder builder = new StringBuilder();

    public static MarkdownMsg builder() {
        return new MarkdownMsg();
    }

    public MarkdownMsg text(String text) {
        builder.append(text);
        return this;
    }

    @Override
    public String build() {
        return builder.toString();
    }

}
