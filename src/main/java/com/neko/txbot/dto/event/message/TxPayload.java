package com.neko.txbot.dto.event.message;


import com.alibaba.fastjson2.JSONObject;
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
public class TxPayload {
    /**
     * ark 模板 id（需要先申请）
     */
    @JSONField(name = "op")
    private Integer op;
    /**
     * kv 值列表
     */
    @JSONField(name = "s")
    private Long s;
    /**
     * kv 值列表
     */
    @JSONField(name = "t")
    private String t;
    /**
     * 数据
     */
    @JSONField(name = "d")
    private JSONObject d;
}
