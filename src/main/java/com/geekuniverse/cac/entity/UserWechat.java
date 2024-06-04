package com.geekuniverse.cac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信用户表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_wechat")
@ApiModel(value = "UserWechat对象", description = "微信用户表")
public class UserWechat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("微信openid")
    private String openid;

    @ApiModelProperty("微信unionid")
    private String unionid;

    @ApiModelProperty("微信session_key")
    private String sessionKey;

    @ApiModelProperty("微信昵称")
    private String nickName;

    @ApiModelProperty("微信头像")
    private String avatarUrl;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("0=普通用户，1=普通会员，2=经营合伙人")
    private Integer vipLevel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("会员开通时间")
    private LocalDateTime vipOpening;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("会员到期时间")
    private LocalDateTime vipExpire;

    @ApiModelProperty("推广二维码")
    private String shareQrCode;

    @ApiModelProperty("推荐人ID，分享二维码中的scene参数")
    private Integer referrerId;

    @ApiModelProperty("充值余额")
    @TableField(exist = false)
    private BigDecimal rechargeBalance = BigDecimal.ZERO;

    @ApiModelProperty("盈利余额")
    @TableField(exist = false)
    private BigDecimal incomeBalance = BigDecimal.ZERO;

    @ApiModelProperty("提现中余额")
    @TableField(exist = false)
    private BigDecimal withdrawingBalance;

    @ApiModelProperty("已提现余额")
    @TableField(exist = false)
    private BigDecimal withdrawnBalance;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
