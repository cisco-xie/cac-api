package com.geekuniverse.cac.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 包厢
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Getter
@Setter
public class StoreBoxDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty("包厢名")
    private String name;

    @ApiModelProperty("门店ID")
    private Integer storeId;

    @ApiModelProperty("所属门店")
    private String storeName;

    @ApiModelProperty("包厢图片,多个逗号分隔")
    private String imgs;

    @ApiModelProperty("包厢简介")
    private String intro;

    @ApiModelProperty("每小时单价")
    private BigDecimal hourlyPrice;

    @ApiModelProperty("出门开关设备ID")
    private String deviceSocket10a;

    @ApiModelProperty("插座16A设备ID")
    private String deviceSocket16a;

    @ApiModelProperty("最少几小时起")
    private Integer minHours;

    @ApiModelProperty("自动关闭设备,0否1是")
    private Integer autoClose;

    @ApiModelProperty("包厢状态,1=空闲 2=已售")
    private Integer status = 1;

    @ApiModelProperty("标签")
    private List<String> labels;

    @ApiModelProperty("不可用时间段")
    private List<UsagePeriodDTO> usagePeriod;

}
