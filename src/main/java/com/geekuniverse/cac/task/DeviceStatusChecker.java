package com.geekuniverse.cac.task;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.constants.Constants;
import com.geekuniverse.cac.entity.Order;
import com.geekuniverse.cac.entity.StoreBox;
import com.geekuniverse.cac.mapper.OrderMapper;
import com.geekuniverse.cac.mapper.StoreBoxMapper;
import com.geekuniverse.cac.thirdparty.unisoft.api.UnisoftApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检查设备是否需要关闭或开启任务
 *
 * @name: OrderStatusChecker
 * @author: 谢诗宏
 * @date: 2023-05-21 16:05
 **/
@Slf4j
@Component
public class DeviceStatusChecker {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private StoreBoxMapper storeBoxMapper;

    @Scheduled(cron = "0/30 * * * * ?")
    public void check() {
        List<StoreBox> storeBoxes = storeBoxMapper.selectList(Wrappers.<StoreBox>lambdaQuery()
                .isNotNull(StoreBox::getDeviceSocket16a)
                .eq(StoreBox::getAutoClose, 1));
        for (StoreBox storeBox : storeBoxes) {
            // 获取当前包厢设备是否存在正在使用中的订单，理论上一个包厢在一个时间段只会有一个订单，这里用list是为了防止出现同时段一个订单的脏数据情况
            List<Order> orders = orderMapper.selectList(Wrappers.<Order>lambdaQuery()
                    .eq(Order::getBoxId, storeBox.getId())
                    .lt(Order::getStartTime, LocalDateTime.now().plusMinutes(30))
                    .gt(Order::getEndTime, LocalDateTime.now())
            );
            // 没有则关闭包厢内总控
            if (CollUtil.isEmpty(orders)) {
                log.info("包厢名{} 包厢id{} 设备ID{},当前时间不存在订单,进行关闭总控操作", storeBox.getName(), storeBox.getId(), storeBox.getDeviceSocket16a());
                JSONObject jsonObject = UnisoftApi.control(storeBox.getDeviceSocket16a(), Constants.DevicePower.CLOSE.getValue(), 0L);
                if (200 == jsonObject.getInteger("code")) {
                    log.info("包厢名{} 包厢id{} 设备ID{},关闭总控操作成功", storeBox.getName(), storeBox.getId(), storeBox.getDeviceSocket16a());
                }
            } else {
                log.info("包厢名{} 包厢id{} 设备ID{},当前时间存在订单", storeBox.getName(), storeBox.getId(), storeBox.getDeviceSocket16a());
            }
        }
    }

}
