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
public class MessageArkKv {
    /**
     * key
     */
    @JSONField(name = "key")
    private String key;
    /**
     * value
     */
    @JSONField(name = "value")
    private String value;
    /**
     * kv 值列表
     */
    @JSONField(name = "obj")
    private ArrayList<MessageArkObj> obj;
}
