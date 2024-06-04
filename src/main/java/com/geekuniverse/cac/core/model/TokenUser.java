package com.geekuniverse.cac.core.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description 用户token信息
 * @author 谢诗宏
 * @date 2022/12/18
 */
@Data
public class TokenUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 账号平台类型。1：google；2：facebook
     */
    private Integer type;

    /**
     * 1=free免费版，2=Basic edition基础版，3=professional edition专业版，4=Flagship旗舰版
     */
    private Integer role;

    /**
     * 平台用户最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 平台用户最后修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 登录类型,APP/PC
     */
    private Integer loginType;

    private String openid;
}
