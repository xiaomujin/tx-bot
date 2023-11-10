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
public class MessageEmbed {
    /**
     * 标题
     */
    @JSONField(name = "title")
    private String title;
    /**
     * 消息弹窗内容
     */
    @JSONField(name = "prompt")
    private String prompt;
    /**
     * 缩略图
     */
    @JSONField(name = "thumbnail")
    private MessageEmbedThumbnail thumbnail;
    /**
     * embed 字段数据
     */
    @JSONField(name = "fields")
    private ArrayList<MessageEmbedField> fields;
}
