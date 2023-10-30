package com.neko.txbot.model;

public record TxPayload(Integer op, Object d, Long s, String t) {
}
