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
@ApiModel(value = "预约下单req", description = "用于门店预约下单的对象")
public class OrderReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "门店ID")
    private Integer oldOrderId;

    @NotNull(message = "门店ID不能为空")
    @ApiModelProperty(value = "门店ID", required = true)
    private Integer storeId;

    @NotNull(message = "包厢ID不能为空")
    @ApiModelProperty(value = "包厢ID", required = true)
    private Integer boxId;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", required = true)
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", required = true)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "用户ID", hidden = true)
    private Integer userId;

    @ApiModelProperty(value = "内部订单号", hidden = true)
    private String orderNo;

    @ApiModelProperty("实际付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "支付方式,1=微信支付，2=钱包支付", hidden = true)
    private Integer payment;

    @ApiModelProperty(value = "是否续费订单,0=否，1=是")
    private Integer renew;

}
