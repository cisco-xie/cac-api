package com.geekuniverse.cac.task;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.constants.Constants;
import com.geekuniverse.cac.common.utils.DateUtil;
import com.geekuniverse.cac.entity.Order;
import com.geekuniverse.cac.entity.StoreBox;
import com.geekuniverse.cac.entity.UserWechat;
import com.geekuniverse.cac.mapper.OrderMapper;
import com.geekuniverse.cac.mapper.StoreBoxMapper;
import com.geekuniverse.cac.mapper.UserWechatMapper;
import com.geekuniverse.cac.thirdparty.ihuyi.api.IhuyiApi;
import com.geekuniverse.cac.thirdparty.unisoft.api.UnisoftApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单到期提醒
 *
 * @name: OrderRemindTask
 * @author: 谢诗宏
 * @date: 2023-05-21 16:05
 **/
@Slf4j
@Component
public class OrderRemindTask {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserWechatMapper userWechatMapper;

    @Scheduled(cron = "0 */30 * * * ?")
    public void check() {
        LocalDateTime now = LocalDateTime.now();
        String endTime = DateUtil.fmt(now.plusMinutes(30), "yyyy-MM-dd HH:mm");
        List<Order> orders = orderMapper.selectList(Wrappers.<Order>lambdaQuery()
                .lt(Order::getStartTime, now)
                .eq(Order::getEndTime, endTime+":00")
        );
        if (!CollUtil.isEmpty(orders)) {
            // 即将到期的订单发送短信通知续费
            orders.forEach(order -> {
                try {
                    UserWechat userWechat = userWechatMapper.selectById(order.getUserId());
                    if (userWechat != null && StringUtils.isNotBlank(userWechat.getPhone())) {
                        IhuyiApi.voice(userWechat.getPhone(), order.getStoreName() + order.getBoxName());
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
