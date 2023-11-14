package com.neko.txbot.dto.event.message;

import com.alibaba.fastjson2.annotation.JSONField;
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
public class ReadyEvent extends Event {
    /**
     * 消息id
     */
    @JSONField(name = "version")
    private Long version;

    /**
     * 子频道 ID
     */
    @JSONField(name = "session_id")
    private String sessionId;

    /**
     * bot用户信息
     */
    @JSONField(name = "user")
    private User user;

    /**
     * 消息内容
     */
    @JSONField(name = "shard")
    private ArrayList<Integer> shard;

}
