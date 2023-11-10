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
public class MessageArkObjKv {
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
}
