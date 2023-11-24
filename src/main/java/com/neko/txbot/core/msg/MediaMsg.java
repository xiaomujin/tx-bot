package com.neko.txbot.core.msg;

import lombok.Getter;

@Getter
public class MediaMsg extends BaseMsg {
    protected String url = "";

    @Override
    public String build() {
        return url;
    }
}
