package com.neko.txbot.core;

import com.neko.txbot.dto.event.message.ChannelMessageEvent;
import com.neko.txbot.dto.event.message.GroupMessageEvent;
import com.neko.txbot.dto.event.message.MessageEvent;

/**
 * <p>BotPlugin class.</p>
 */
public class BotPlugin {

    /**
     * 向下执行
     */
    public static final int MESSAGE_IGNORE = 0;

    /**
     * 不向下执行
     */
    public static final int MESSAGE_BLOCK = 1;

    /**
     * 收到频道群聊消息
     *
     * @param bot   {@link Bot}
     * @param event {@link MessageEvent}
     * @return 是否执行下一个插件，MESSAGE_IGNORE 向下执行，MESSAGE_BLOCK 不向下执行
     */
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        return MESSAGE_IGNORE;
    }

    /**
     * 收到群聊消息
     *
     * @param bot   {@link Bot}
     * @param event {@link MessageEvent}
     * @return 是否执行下一个插件，MESSAGE_IGNORE 向下执行，MESSAGE_BLOCK 不向下执行
     */
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        return MESSAGE_IGNORE;
    }


}
