package com.neko.txbot.core.msg;

public class ImgMsg extends MediaMsg {
    private boolean proxy = false;
//    public static final String PROXY_IMG_URL = "https://i3.wp.com/";
    public static final String PROXY_IMG_URL = "https://ct-game.com/rp/file/";

    public ImgMsg img(String url) {
        this.url = url;
        return this;
    }

    public ImgMsg proxy() {
        this.proxy = true;
        return this;
    }

    public static ImgMsg builder() {
        return new ImgMsg();
    }

    @Override
    public String build() {
        if (proxy) {
            url = url.replaceFirst("^https?://", "");
            url = PROXY_IMG_URL + url;
        }
        return url;
    }
}
