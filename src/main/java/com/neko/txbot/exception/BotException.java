package com.neko.txbot.exception;

public class BotException extends RuntimeException {
    public BotException(String message) {
        super(message);
    }

    public BotException() {
        super();
    }
}
