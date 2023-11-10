package com.neko.txbot.dto.event.message;


import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class MessageReference {
    /**
     * 需要引用回复的消息 ID
     */
    @JSONField(name = "message_id")
    private String messageId;
    /**
     * 是否忽略获取引用消息详情错误，默认否
     */
    @JSONField(name = "ignore_get_message_error")
    private Boolean ignoreGetMessageError;
}
