package com.neko.txbot.menu;

public class TxApi {
    public final static String MAIN_URL = "https://api.sgroup.qq.com/";
    public final static String SANDBOX_URL = "https://sandbox.api.sgroup.qq.com/";
    public final static String URL = SANDBOX_URL;

    public final static String GET_APP_ACCESS_TOKEN = "https://bots.qq.com/app/getAppAccessToken";
    public final static String GATEWAY = URL + "gateway";
    public final static String TEXT_SUB_CHANNEL = URL + "v2/channels/{channel_id}/messages";
}