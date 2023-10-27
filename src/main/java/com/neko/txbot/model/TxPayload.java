package com.neko.txbot.model;

import com.alibaba.fastjson2.JSONObject;

public record TxPayload(Integer op, JSONObject d, Long s, String t) {
}
