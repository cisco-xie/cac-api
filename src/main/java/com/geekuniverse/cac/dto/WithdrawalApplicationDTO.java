package com.geekuniverse.cac.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户提现申请表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-07-20
 */
@Getter
@Setter
@Accessors(chain = true)
public class WithdrawalApplicationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("用户头像")
    private String avatarUrl;

    @ApiModelProperty("用户名")
    private String nickname;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("申请提现金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty("用户收款二维码")
    private String paymentCode;

    @ApiModelProperty("1提现申请中，2拒绝提现，3提现成功")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
