package com.geekuniverse.cac.req;

import com.geekuniverse.cac.core.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel(value = "门店列表req", description = "用于搜索门店的对象")
public class StoreReq extends PageReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "门店名称", required = false)
    private String name;

    @NotNull(message = "用户当前经度必填")
    @ApiModelProperty(value = "用户当前经度", required = true)
    private BigDecimal lng;

    @NotNull(message = "用户当前纬度必填")
    @ApiModelProperty(value = "用户当前纬度", required = true)
    private BigDecimal lat;

}
