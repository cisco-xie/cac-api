package com.geekuniverse.cac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@TableName("t_withdrawal_application")
@ApiModel(value = "WithdrawalApplication对象", description = "用户提现申请表")
public class WithdrawalApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("申请提现金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty("用户收款二维码")
    private String paymentCode;

    @ApiModelProperty("1提现申请中，2拒绝提现，3提现成功")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
