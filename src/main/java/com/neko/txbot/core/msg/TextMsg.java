package com.neko.txbot.core.msg;

public class TextMsg extends BaseMsg {
    private final StringBuilder builder = new StringBuilder();

    public static TextMsg builder() {
        return new TextMsg();
    }

    public TextMsg text(String text) {
        builder.append(text);
        return this;
    }

    @Override
    public String build() {
        return builder.toString();
    }

}
