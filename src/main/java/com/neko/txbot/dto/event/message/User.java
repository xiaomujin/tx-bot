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
public class User {
    /**
     * 用户 ID
     */
    @JSONField(name = "id")
    private String id;
    /**
     * 用户名
     */
    @JSONField(name = "username")
    private String username;
    /**
     * 用户头像地址
     */
    @JSONField(name = "avatar")
    private String avatar;
    /**
     * 是否是机器人
     */
    @JSONField(name = "bot")
    private Boolean bot;
    /**
     * 特殊关联应用的 openid，需要特殊申请并配置后才会返回。如需申请，请联系平台运营人员。
     */
    @JSONField(name = "union_openid")
    private String unionOpenid;
    /**
     * 机器人关联的互联应用的用户信息，与 union_openid 关联的应用是同一个。如需申请，请联系平台运营人员。
     */
    @JSONField(name = "union_user_account")
    private String unionUserAccount;
    /**
     * 机器人 READY事件返回的状态
     */
    @JSONField(name = "status")
    private Integer status;
}
