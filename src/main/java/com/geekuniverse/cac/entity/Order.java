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
 * 订单表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_order")
@ApiModel(value = "Order对象", description = "订单表")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("退款单号")
    private String refundNo;

    @ApiModelProperty("门店ID")
    private Integer storeId;

    @ApiModelProperty("包厢ID")
    private Integer boxId;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("包厢名称")
    private String boxName;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("门店地址")
    private String address;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("总时长")
    private BigDecimal duration;

    @ApiModelProperty("实际付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("微信支付订单号")
    private String transactionId;

    @ApiModelProperty(value = "支付状态,1=已支付，2=已退款")
    private Integer payStatus;

    @ApiModelProperty(value = "支付方式,1=微信支付，2=钱包支付", hidden = true)
    private Integer payment;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
