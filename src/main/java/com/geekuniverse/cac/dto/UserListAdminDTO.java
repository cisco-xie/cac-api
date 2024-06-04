package com.geekuniverse.cac.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserListAdminDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("0=普通用户，1=普通会员，2=经营合伙人")
    private Integer vipLevel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("会员开通时间")
    private LocalDateTime vipOpening;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("会员到期时间")
    private LocalDateTime vipExpire;

    @ApiModelProperty("充值余额")
    @TableField(exist = false)
    private BigDecimal rechargeBalance = BigDecimal.ZERO;

    @ApiModelProperty("盈利余额")
    @TableField(exist = false)
    private BigDecimal incomeBalance = BigDecimal.ZERO;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("注册时间")
    private LocalDateTime createTime;
}
