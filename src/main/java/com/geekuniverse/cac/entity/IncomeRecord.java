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
 * 合伙人收益记录表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-06-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_income_record")
@ApiModel(value = "IncomeRecord对象", description = "合伙人收益记录表")
public class IncomeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("合伙人用户ID")
    private Integer userId;

    @ApiModelProperty("收益金额")
    private BigDecimal incomeAmount;

    @ApiModelProperty("收益比例")
    private BigDecimal incomeScale;

    @ApiModelProperty("来源订单ID")
    private Integer orderId;

    @ApiModelProperty("来源订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("来源下单用户ID")
    private Integer orderUserId;

    @ApiModelProperty("下单时间")
    private LocalDateTime orderTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
