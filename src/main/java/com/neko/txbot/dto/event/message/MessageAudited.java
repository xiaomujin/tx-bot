package com.neko.txbot.dto.event.message;


import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class MessageAudited {
    /**
     * 消息审核 ID ID
     */
    @JSONField(name = "audit_id")
    private String auditId;
    /**
     * 消息 ID，只有审核通过事件才会有值
     */
    @JSONField(name = "message_id")
    private String messageId;
    /**
     * 消息 ID，只有审核通过事件才会有值
     */
    @JSONField(name = "guild_id")
    private String guildId;
    /**
     * 子频道 ID
     */
    @JSONField(name = "channel_id")
    private String channelId;
    /**
     * 消息审核时间，是个 ISO8601 timestamp 字符串，例："2021-11-23T15:16:48+08:00"
     */
    @JSONField(name = "audit_time")
    private String auditTime;
    /**
     * 消息创建时间，是个 ISO8601 timestamp 字符串，例："2021-11-23T15:16:48+08:00"
     */
    @JSONField(name = "create_time")
    private String createTime;
    /**
     * 子频道消息 seq，用于消息间的排序，seq 在同一子频道中按从先到后的顺序递增，不同的子频道之前消息无法排序
     */
    @JSONField(name = "seq_in_channel")
    private String seqInChannel;

}
