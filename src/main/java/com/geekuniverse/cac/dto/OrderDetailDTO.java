package com.geekuniverse.cac.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单详情
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Getter
@Setter
public class OrderDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty("包厢名称")
    private String boxName;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("包厢id")
    private Integer boxId;

    @ApiModelProperty("门店id")
    private Integer storeId;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("总时长")
    private BigDecimal duration;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("实际付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("包厢图,多个逗号分隔")
    private String imgs;

    @ApiModelProperty("用户名")
    private String nickName;

    @ApiModelProperty("下单时间")
    private LocalDateTime createTime;

}
