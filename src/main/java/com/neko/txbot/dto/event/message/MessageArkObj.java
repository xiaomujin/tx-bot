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
public class MessageArkObj {
    /**
     * ark objkv 列表
     */
    @JSONField(name = "obj_kv")
    private MessageArkObjKv obj_kv;
}
