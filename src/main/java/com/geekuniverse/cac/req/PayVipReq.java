package com.geekuniverse.cac.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * <p>
 * 预约下单req
 * </p>
 *
 * @author 谢诗宏
 * @since 2022-12-07
 */
@Data
@ApiModel(value = "充值会员req", description = "用于用户充值会员的对象")
public class PayVipReq {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "门店ID不能为空")
    @ApiModelProperty(value = "1=普通会员，2=经营合伙人", required = true)
    private Integer vipLevel;

}
