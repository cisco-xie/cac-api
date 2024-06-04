package com.geekuniverse.cac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 包厢表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_store_box")
@ApiModel(value = "StoreBox对象", description = "包厢表")
public class StoreBox implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("门店ID")
    private Integer storeId;

    @ApiModelProperty("包厢名称")
    private String name;

    @ApiModelProperty("包厢简介")
    private String intro;

    @ApiModelProperty("每小时单价")
    private BigDecimal hourlyPrice;

    @ApiModelProperty("包厢图片,多个逗号分隔")
    private String imgs;

    @ApiModelProperty("出门开关设备ID")
    private String deviceDoor;

    @ApiModelProperty("插座10A设备ID")
    @TableField("device_socket_10a")
    private String deviceSocket10a;

    @ApiModelProperty("插座16A设备ID")
    @TableField("device_socket_16a")
    private String deviceSocket16a;

    @ApiModelProperty("自动关闭设备,0否1是")
    private Integer autoClose;

    @ApiModelProperty("最少几小时起")
    private Integer minHours;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
