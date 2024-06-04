package com.geekuniverse.cac.req;

import com.geekuniverse.cac.core.req.PageReq;
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
@ApiModel(value = "门店req", description = "用于新增门店的对象")
public class StoreAddReq {

    @ApiModelProperty(value = "id")
    private Integer id;

    @NotBlank(message = "门店名称必填")
    @ApiModelProperty(value = "门店名称")
    private String name;

    @NotBlank(message = "门店封面必填")
    @ApiModelProperty("门店封面")
    private String cover;

    @NotBlank(message = "地址必填")
    @ApiModelProperty("地址")
    private String address;

    @NotNull(message = "用户当前经度必填")
    @ApiModelProperty(value = "用户当前经度", required = true)
    private BigDecimal lng;

    @NotNull(message = "用户当前纬度必填")
    @ApiModelProperty(value = "用户当前纬度", required = true)
    private BigDecimal lat;

    @NotBlank(message = "电话必填")
    @ApiModelProperty("电话")
    private String phone;

    @NotNull(message = "几小时起必填")
    @ApiModelProperty("最少几小时起")
    private Integer minHours;

}
