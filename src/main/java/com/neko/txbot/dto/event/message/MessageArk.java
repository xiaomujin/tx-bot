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
public class MessageArk {
    /**
     * ark 模板 id（需要先申请）
     */
    @JSONField(name = "template_id")
    private String templateId;
    /**
     * kv 值列表
     */
    @JSONField(name = "kv")
    private ArrayList<MessageArkKv> kv;
}
