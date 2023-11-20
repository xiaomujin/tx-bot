package com.neko.txbot.menu;

import lombok.Getter;

import java.util.List;

@Getter
public enum Intent {
    GUILDS(0),
    GUILD_MEMBERS(1),
    /**
     * 私域
     */
    GUILD_MESSAGES(9),
    GUILD_MESSAGE_REACTIONS(10),
    DIRECT_MESSAGE(12),
    GROUP(25),
    INTERACTION(26),
    MESSAGE_AUDIT(27),
    /**
     * 私域
     */
    FORUMS_EVENT(28),
    AUDIO_ACTION(29),
    PUBLIC_GUILD_MESSAGES(30),

    ;

    private final int pos;

    Intent(int pos) {
        this.pos = pos;
    }

    public static long calcList(List<Intent> intents) {
        if (intents == null || intents.isEmpty()) {
            intents = List.of(Intent.values());
        }
        return intents.stream().map(Intent::calcOne).reduce(0L, (n1, n2) -> n1 | n2);
    }

    public static long calcOne(Intent intent) {
        return 1L << intent.pos;
    }

}
