package com.geekuniverse.cac.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class VipSetupReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Integer id;

    @NotNull(message = "充值金额不能为空")
    @ApiModelProperty("充值金额")
    private BigDecimal rechargeAmount;

    @NotNull(message = "折扣比例不能为空")
    @ApiModelProperty("折扣比例")
    private BigDecimal discountScale;

    @ApiModelProperty("收益比例")
    private BigDecimal incomeScale;

}
