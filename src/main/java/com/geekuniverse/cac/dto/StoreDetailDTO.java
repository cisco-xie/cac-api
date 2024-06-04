package com.geekuniverse.cac.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 门店表
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Getter
@Setter
public class StoreDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty("门店名")
    private String name;

    @ApiModelProperty("门店封面")
    private String cover;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("最少几小时起")
    private Integer minHours;

    @ApiModelProperty("包厢集合")
    private List<StoreBoxDTO> boxs;
}
