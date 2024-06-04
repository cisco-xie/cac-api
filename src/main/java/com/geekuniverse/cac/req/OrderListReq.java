package com.geekuniverse.cac.req;

import com.geekuniverse.cac.core.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 门店列表req
 * </p>
 *
 * @author 谢诗宏
 * @since 2022-12-07
 */
@Data
@ApiModel(value = "订单列表req", description = "用于搜索订单的对象")
public class OrderListReq extends PageReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("门店id")
    private Integer storeId;

    @ApiModelProperty(value = "订单状态,0=全部订单 1=未开始 2=进行中 3=已完成 4=已取消", required = false)
    private Integer status;

    @ApiModelProperty(value = "排序方式,default=默认 orderTime=下单时间 startTime=预约时间", required = false)
    private String sortOrder;

    @ApiModelProperty(value = "用户id", hidden = true)
    private Integer userId;

    @ApiModelProperty("包厢名称")
    private String boxName;

    @ApiModelProperty("门店名称")
    private String storeName;

}
