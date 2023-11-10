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
public class Member {
    /**
     * 用户的频道基础信息，只有成员相关接口中会填充此信息
     */
    @JSONField(name = "user")
    private User user;
    /**
     * 用户在频道内的昵称
     */
    @JSONField(name = "nick")
    private String nick;
    /**
     * 用户在频道内的身份组 ID，默认值可参考 <a href="https://bot.q.qq.com/wiki/develop/nodesdk/model/role.html#DefaultRoleIDs">DefaultRoleIDs</a>
     */
    @JSONField(name = "roles")
    private ArrayList<String> roles;
    /**
     * 用户加入频道的时间，是个 ISO8601 timestamp 字符串，例："2021-11-23T15:16:48+08:00"
     */
    @JSONField(name = "joined_at")
    private String joinedAt;
}
