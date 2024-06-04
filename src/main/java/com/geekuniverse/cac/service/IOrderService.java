package com.geekuniverse.cac.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geekuniverse.cac.common.bean.wxpay.PayInfoDto;
import com.geekuniverse.cac.core.model.TokenUser;
import com.geekuniverse.cac.dto.OrderDetailDTO;
import com.geekuniverse.cac.dto.OrderListDTO;
import com.geekuniverse.cac.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geekuniverse.cac.req.OrderListReq;
import com.geekuniverse.cac.req.OrderReq;

import java.net.http.HttpRequest;
import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-17
 */
public interface IOrderService extends IService<Order> {

    /**
     * 支付成功创建订单
     *
     * @param req 要求事情
     */
    void paySuccess(OrderReq req);

    /**
     * 获取订单列表页面
     *
     * @param req 筛选条件
     * @return {@link IPage}<{@link OrderListDTO}>
     */
    IPage<OrderListDTO> listPage(OrderListReq req);

    /**
     * 获取订单详情页面
     *
     * @param id
     * @return {@link IPage}<{@link OrderListDTO}>
     */
    OrderDetailDTO detail(Integer id, String nickname);
}
