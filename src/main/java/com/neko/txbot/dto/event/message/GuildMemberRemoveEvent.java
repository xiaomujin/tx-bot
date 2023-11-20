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
public class GuildMemberRemoveEvent extends Event {
    /**
     * 频道id
     */
    @JSONField(name = "guild_id")
    private String guildId;

    /**
     * 加入时间
     */
    @JSONField(name = "joined_at")
    private String joinedAt;

    /**
     * 昵称
     */
    @JSONField(name = "nick")
    private String nick;

    /**
     * 操作用户id
     */
    @JSONField(name = "op_user_id")
    private String opUserId;
    /**
     * 角色
     */
    @JSONField(name = "roles")
    private ArrayList<String> roles;
    /**
     * 类型
     */
    @JSONField(name = "source_type")
    private String sourceType;

    /**
     * 加入者信息
     */
    @JSONField(name = "user")
    private User user;

}
