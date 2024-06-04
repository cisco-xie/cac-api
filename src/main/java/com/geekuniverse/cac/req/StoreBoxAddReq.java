package com.geekuniverse.cac.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * <p>
 * 门店列表req
 * </p>
 *
 * @author 谢诗宏
 * @since 2022-12-07
 */
@Data
@ApiModel(value = "门店包厢req", description = "用于新增门店包厢的对象")
public class StoreBoxAddReq {

    @ApiModelProperty(value = "id")
    private Integer id;

    @NotNull(message = "门店id必填")
    @ApiModelProperty("门店ID")
    private Integer storeId;

    @NotBlank(message = "包厢名称必填")
    @ApiModelProperty("包厢名称")
    private String name;

    @ApiModelProperty("包厢简介")
    private String intro;

    @NotNull(message = "单价必填")
    @ApiModelProperty("每小时单价")
    private BigDecimal hourlyPrice;

    @NotBlank(message = "开关设备必填")
    @ApiModelProperty("出门开关设备ID")
    private String deviceSocket10a;

    @NotBlank(message = "包厢总控必填")
    @ApiModelProperty("插座16A设备ID")
    private String deviceSocket16a;

    @NotNull(message = "最少几小时必填")
    @ApiModelProperty("最少几小时起")
    private Integer minHours;

    @ApiModelProperty("自动关闭设备,0否1是")
    private Integer autoClose;

    @NotBlank(message = "包厢图片必填")
    @ApiModelProperty("包厢图片,多个逗号分隔")
    private String imgs;

}
