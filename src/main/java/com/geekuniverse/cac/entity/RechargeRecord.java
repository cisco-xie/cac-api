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
 * 会员充值记录表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-06-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_recharge_record")
@ApiModel(value = "RechargeRecord对象", description = "会员充值记录表")
public class RechargeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("订单号")
    private String orderNo;

    private Integer userId;

    @ApiModelProperty("充值金额")
    private BigDecimal rechargeAmount;

    @ApiModelProperty("充值会员等级")
    private Integer rechargeVipLevel;

    @ApiModelProperty("充值时间")
    private LocalDateTime rechargeTime;
}
