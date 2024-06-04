package com.geekuniverse.cac.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单列表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Getter
@Setter
public class OrderListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("包厢名称")
    private String boxName;

    @ApiModelProperty("包厢封面")
    private String boxCover;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("总时长")
    private BigDecimal duration;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("实际付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty(value = "支付状态,1=已支付，2=已退款")
    private Integer payStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
