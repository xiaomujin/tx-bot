package com.neko.txbot.dto.event.message;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelMessageEvent extends MessageEvent {
    /**
     * 子频道 ID
     */
    @JSONField(name = "channel_id")
    private String channelId;

    /**
     * 频道 ID
     */
    @JSONField(name = "guild_id")
    private String guildId;
}
