package com.neko.txbot.core;

import com.neko.txbot.dto.event.message.*;

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
     * @param event {@link ChannelMessageEvent}
     * @return 是否执行下一个插件，MESSAGE_IGNORE 向下执行，MESSAGE_BLOCK 不向下执行
     */
    public int onChannelMessage(Bot bot, ChannelMessageEvent event) {
        return MESSAGE_IGNORE;
    }

    /**
     * 收到群聊消息
     *
     * @param bot   {@link Bot}
     * @param event {@link GroupMessageEvent}
     * @return 是否执行下一个插件，MESSAGE_IGNORE 向下执行，MESSAGE_BLOCK 不向下执行
     */
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        return MESSAGE_IGNORE;
    }

    /**
     * 用户加入频道
     *
     * @param bot   {@link Bot}
     * @param event {@link GuildMemberAddEvent}
     * @return 是否执行下一个插件，MESSAGE_IGNORE 向下执行，MESSAGE_BLOCK 不向下执行
     */
    public int onGuildMemberAdd(Bot bot, GuildMemberAddEvent event) {
        return MESSAGE_IGNORE;
    }

    /**
     * 用户退出频道
     *
     * @param bot   {@link Bot}
     * @param event {@link GuildMemberRemoveEvent}
     * @return 是否执行下一个插件，MESSAGE_IGNORE 向下执行，MESSAGE_BLOCK 不向下执行
     */
    public int onGuildMemberRemove(Bot bot, GuildMemberRemoveEvent event) {
        return MESSAGE_IGNORE;
    }


}
