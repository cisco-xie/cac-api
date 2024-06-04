package com.geekuniverse.cac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geekuniverse.cac.common.bean.wxpay.PayInfoDto;
import com.geekuniverse.cac.common.constants.Constants;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.model.TokenUser;
import com.geekuniverse.cac.dto.OrderDetailDTO;
import com.geekuniverse.cac.dto.OrderListDTO;
import com.geekuniverse.cac.entity.*;
import com.geekuniverse.cac.mapper.OrderMapper;
import com.geekuniverse.cac.mapper.StoreBoxMapper;
import com.geekuniverse.cac.mapper.StoreMapper;
import com.geekuniverse.cac.req.OrderListReq;
import com.geekuniverse.cac.req.OrderReq;
import com.geekuniverse.cac.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-17
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private IUserWechatService userWechatService;
    @Resource
    private IIncomeRecordService incomeRecordService;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private StoreBoxMapper storeBoxMapper;

    @Resource
    private IBalanceService balanceService;

    @Resource
    private IVipSetupService vipSetupService;

    @Override
    public void paySuccess(OrderReq req) {
        Store store = storeMapper.selectById(req.getStoreId());
        StoreBox storeBox = storeBoxMapper.selectById(req.getBoxId());
        // 支付单号
        Duration between = LocalDateTimeUtil.between(req.getStartTime(), req.getEndTime());
        BigDecimal twoDecimal = new BigDecimal(between.toMinutes());
        BigDecimal hour = twoDecimal.divide(BigDecimal.valueOf(60),2, RoundingMode.HALF_UP);
        //BigDecimal payAmount = storeBox.getHourlyPrice().multiply(hour);
        // 修改订单信息
        Order order = new Order();
        order.setOrderNo(req.getOrderNo());
        order.setStoreId(req.getStoreId());
        order.setBoxId(req.getBoxId());
        order.setStoreName(store.getName());
        order.setBoxName(storeBox.getName());
        order.setUserId(req.getUserId());
        order.setAddress(store.getAddress());
        order.setLng(store.getLng());
        order.setLat(store.getLat());
        order.setPhone(store.getPhone());
        order.setStartTime(req.getStartTime());
        order.setEndTime(req.getEndTime());
        order.setDuration(hour);
        order.setPaymentAmount(req.getPaymentAmount());
        order.setPayment(req.getPayment());
        order.setPayStatus(1);

        baseMapper.insert(order);

        UserWechat userWechat = userWechatService.getById(req.getUserId());
        if (null != userWechat.getReferrerId()) {
            VipSetup vipSetup = vipSetupService.getOne(Wrappers.<VipSetup>lambdaQuery().eq(VipSetup::getVipLevel, 2));
            // 如果用户存在推荐人ID,则记录推荐人收益
            IncomeRecord incomeRecord = new IncomeRecord();
            incomeRecord.setUserId(userWechat.getReferrerId());
            // 合伙人按1成比例收成
            incomeRecord.setIncomeAmount(order.getPaymentAmount().multiply(vipSetup.getIncomeScale().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            incomeRecord.setIncomeScale(vipSetup.getIncomeScale());
            incomeRecord.setOrderId(order.getId());
            incomeRecord.setOrderUserId(order.getUserId());
            incomeRecord.setOrderAmount(order.getPaymentAmount());
            incomeRecord.setOrderTime(LocalDateTime.now());
            incomeRecordService.save(incomeRecord);

            Balance balance = balanceService.getOne(Wrappers.<Balance>lambdaQuery().eq(Balance::getUserId, userWechat.getReferrerId()));

            // 更新收益余额
            if (balance != null) {
                balance.setIncomeBalance(balance.getIncomeBalance().add(incomeRecord.getIncomeAmount()));
                balanceService.updateById(balance);
            } else {
                balance = new Balance();
                balance.setUserId(userWechat.getReferrerId());
                balance.setIncomeBalance(incomeRecord.getIncomeAmount());
                balanceService.save(balance);
            }

        }
    }

    @Override
    public IPage<OrderListDTO> listPage(OrderListReq req) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", req.getUserId());
        wrapper.eq(req.getStoreId() != null, "store_id", req.getStoreId());
        if ("default".equals(req.getSortOrder()) || "orderTime".equals(req.getSortOrder())) {
            wrapper.orderBy(true, false, "create_time");
        } else if ("startTime".equals(req.getSortOrder())) {
            wrapper.orderBy(true, true, "start_time");
        }
        IPage<Order> list = baseMapper.selectPage(new Page<>(req.getPageIndex(), req.getPageSize()), wrapper);

        IPage<OrderListDTO> result = list.convert(order -> BeanUtil.copyProperties(order, OrderListDTO.class));
        result.getRecords().forEach(record ->{
            UserWechat userWechat = userWechatService.getById(record.getUserId());
            if (userWechat != null) {
                record.setNickName(userWechat.getNickName());
            }
            StoreBox box = storeBoxMapper.selectById(record.getBoxId());
            if (box != null) {
                record.setBoxCover(box.getImgs());
            }
        });
        return result;
    }

    @Override
    public OrderDetailDTO detail(Integer id, String nickname) {
        Order order = baseMapper.selectById(id);
        OrderDetailDTO result = BeanUtil.copyProperties(order, OrderDetailDTO.class);
        if (order == null) {
            throw new BusinessException(SystemError.ORDER_2404);
        }
        StoreBox box = storeBoxMapper.selectById(order.getBoxId());
        result.setImgs(box != null ? box.getImgs() : null);
        result.setNickName(nickname);
        return result;
    }

}
