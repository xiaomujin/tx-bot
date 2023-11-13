package com.neko.txbot.dto.event.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neko.txbot.dto.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class MessageEvent extends Event {
    /**
     * 消息id
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 消息内容
     */
    @JSONField(name = "content")
    private String content;

    /**
     * 消息创建时间
     */
    @JSONField(name = "timestamp")
    private String timestamp;
    /**
     * 消息编辑时间
     */
    @JSONField(name = "edited_timestamp")
    private String editedTimestamp;
    /**
     * 是否@全体成员
     */
    @JSONField(name = "mention_everyone")
    private String mentionEveryone;

    /**
     * 消息创建者
     */
    @JSONField(name = "author")
    private User author;

    /**
     * 附件
     */
    @JSONField(name = "attachments")
    private ArrayList<MessageAttachment> attachments;

    /**
     * embed
     */
    @JSONField(name = "embeds")
    private ArrayList<MessageEmbed> embeds;

    /**
     * 消息中@的人
     */
    @JSONField(name = "mentions")
    private ArrayList<User> mentions;

    /**
     * 消息创建者的 member 信息
     */
    @JSONField(name = "member")
    private Member member;

    /**
     * ark 消息
     */
    @JSONField(name = "ark")
    private MessageArk ark;

    /**
     * 用于消息间的排序，seq 在同一子频道中按从先到后的顺序递增，不同的子频道之前消息无法排序。(目前只在消息事件中有值，后续废弃)
     */
    @JSONField(name = "seq")
    private Long seq;

    /**
     * 子频道消息 seq，用于消息间的排序，seq 在同一子频道中按从先到后的顺序递增，不同的子频道之前消息无法排序
     */
    @JSONField(name = "seq_in_channel")
    private String seqInChannel;

    /**
     * 引用消息对象
     */
    @JSONField(name = "message_reference")
    private MessageReference messageReference;

    /**
     * 无@的消息内容
     */
    @JsonIgnore
    private String noAtContent;

}
