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
 * 会员充值字典表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-07-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_vip_setup")
@ApiModel(value = "VipSetup对象", description = "会员充值字典表")
public class VipSetup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("充值金额")
    private BigDecimal rechargeAmount;

    @ApiModelProperty("折扣比例")
    private BigDecimal discountScale;

    @ApiModelProperty("收益比例")
    private BigDecimal incomeScale;

    @ApiModelProperty("0=普通用户，1=普通会员，2=经营合伙人")
    private Integer vipLevel;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
