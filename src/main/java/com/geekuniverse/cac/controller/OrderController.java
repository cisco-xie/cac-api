package com.geekuniverse.cac.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.bean.wxpay.PayInfoDto;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.config.WxPayConfiguration;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.result.Result;
import com.geekuniverse.cac.core.support.BaseController;
import com.geekuniverse.cac.dto.OrderDetailDTO;
import com.geekuniverse.cac.dto.OrderListDTO;
import com.geekuniverse.cac.entity.*;
import com.geekuniverse.cac.mapper.OrderMapper;
import com.geekuniverse.cac.mapper.StoreBoxMapper;
import com.geekuniverse.cac.mapper.StoreMapper;
import com.geekuniverse.cac.req.OrderListReq;
import com.geekuniverse.cac.req.OrderReq;
import com.geekuniverse.cac.req.PayVipReq;
import com.geekuniverse.cac.service.*;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-17
 */
@Slf4j
@Api(tags = "订单模块")
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource
    private IUserWechatService userWechatService;
    @Resource
    private IOrderService orderService;
    @Resource
    private IRechargeRecordService rechargeRecordService;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private StoreBoxMapper storeBoxMapper;

    @Resource
    private WxPayConfiguration wxPayConfiguration;

    @Resource
    private WxPayService wxService;

    @Resource
    private IBalanceService balanceService;

    @Resource
    private IVipSetupService vipSetupService;

    @Resource
    private WxPayService wxMiniPayService;

    @Value("${thirdparty.wechat.notify.refund}")
    private String REFUNDURL;

    @ApiOperation(value = "预约门店付款-钱包支付")
    @PostMapping("/payment/store/wallet")
    public Result pay(@RequestBody @Validated OrderReq req){

        // 开始时间和结束时间根据需求获取
        LocalDateTime startTime;
        LocalDateTime endTime;
        if (req.getRenew() == 1) {
            endTime = req.getEndTime();
            startTime = req.getStartTime();

            Order order = orderService.getById(req.getOldOrderId());
            if (!order.getEndTime().isEqual(startTime)) {
                throw new BusinessException(SystemError.ORDER_2003);
            }
        } else {
            // 如果不是续费订单，开始时间和结束时间前后各控制半小时区间，目的是为了已有订单的前后半小时无法预约
            startTime = req.getStartTime().plusMinutes(-30);
            endTime = req.getEndTime().plusMinutes(30);
        }
        List<Order> orders = orderService.list(Wrappers.<Order>lambdaQuery()
                .eq(Order::getStoreId, req.getStoreId())
                .eq(Order::getBoxId, req.getBoxId())
                .and(wrapper ->
                        wrapper.or(w ->
                                w.gt(Order::getStartTime, startTime)
                                        .lt(Order::getStartTime, endTime)
                        ).or(w ->
                                w.gt(Order::getEndTime, startTime)
                                        .lt(Order::getEndTime, endTime)
                        )
                )
        );
        if (CollUtil.isNotEmpty(orders)) {
            throw new BusinessException(SystemError.ORDER_2000);
        }

        UserWechat userWechat = userWechatService.getById(getUser().getId());
        Store store = storeMapper.selectById(req.getStoreId());
        StoreBox storeBox = storeBoxMapper.selectById(req.getBoxId());

        // 获取用户对应折扣
        VipSetup vipSetup = vipSetupService.getOne(Wrappers.<VipSetup>lambdaQuery().eq(VipSetup::getVipLevel, userWechat.getVipLevel()));

        String orderNo = IdUtil.getSnowflake(1, 1).nextIdStr();
        Duration between = LocalDateTimeUtil.between(req.getStartTime(), req.getEndTime());
        BigDecimal twoDecimal = new BigDecimal(between.toMinutes());
        BigDecimal hour = twoDecimal.divide(BigDecimal.valueOf(60),2, RoundingMode.HALF_UP);
        BigDecimal payAmount = storeBox.getHourlyPrice().multiply(hour);
        BigDecimal discountAmount = payAmount.multiply(vipSetup.getDiscountScale().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        // 扣减折扣
        if (hour.compareTo(new BigDecimal(storeBox.getMinHours())) < 0 && (req.getRenew() == null || req.getRenew() == 0)) {
            throw new BusinessException(SystemError.ORDER_2001, storeBox.getMinHours());
        }

        // 计算当前钱包是否充足并且进行对应扣款
        Balance balance = balanceService.getOne(Wrappers.<Balance>lambdaQuery().eq(Balance::getUserId, getUser().getId()));
        if (balance == null) {
            throw new BusinessException(SystemError.ORDER_2002);
        } else {
            if (balance.getRechargeBalance().compareTo(discountAmount) < 0) {
                throw new BusinessException(SystemError.ORDER_2002);
            }
            balance.setRechargeBalance(balance.getRechargeBalance().subtract(discountAmount));
            // 扣减余额
            balanceService.updateById(balance);
        }
        // 扣减余额成功进行预约包厢订单操作
        OrderReq orderReq = new OrderReq();
        orderReq.setStoreId(req.getStoreId());
        orderReq.setBoxId(req.getBoxId());
        orderReq.setStartTime(req.getStartTime());
        orderReq.setEndTime(req.getEndTime());
        orderReq.setOrderNo(orderNo);
        orderReq.setUserId(userWechat.getId());
        orderReq.setPayment(2);
        orderReq.setPaymentAmount(discountAmount);
        // 支付成功后再创建订单
        orderService.paySuccess(orderReq);

        return Result.success();
    }

    @ApiOperation(value = "预约门店付款-微信支付")
    @PostMapping("/payment/store")
    public Result<WxPayMpOrderResult> store(@RequestBody @Validated OrderReq req){

        // 开始时间和结束时间根据需求获取
        LocalDateTime startTime;
        LocalDateTime endTime;
        if (req.getRenew() == 1) {
            endTime = req.getEndTime();
            startTime = req.getStartTime();
            // 如果是续费订单，则校验开始时间是否和原订单的结束时间相等
            Order order = orderService.getById(req.getOldOrderId());
            if (!order.getEndTime().isEqual(startTime)) {
                throw new BusinessException(SystemError.ORDER_2003);
            }
        } else {
            // 如果不是续费订单，开始时间和结束时间前后各控制半小时区间，目的是为了已有订单的前后半小时无法预约
            startTime = req.getStartTime().plusMinutes(-30);
            endTime = req.getEndTime().plusMinutes(30);
        }

        List<Order> orders = orderService.list(Wrappers.<Order>lambdaQuery()
                .eq(Order::getStoreId, req.getStoreId())
                .eq(Order::getBoxId, req.getBoxId())
                .and(wrapper ->
                        wrapper.or(w ->
                                w.gt(Order::getStartTime, startTime)
                                        .lt(Order::getStartTime, endTime)
                        ).or(w ->
                                w.gt(Order::getEndTime, startTime)
                                        .lt(Order::getEndTime, endTime)
                        )
                )
        );
        if (CollUtil.isNotEmpty(orders)) {
            throw new BusinessException(SystemError.ORDER_2000);
        }

        UserWechat userWechat = userWechatService.getById(getUser().getId());
        Store store = storeMapper.selectById(req.getStoreId());
        StoreBox storeBox = storeBoxMapper.selectById(req.getBoxId());

        // 获取用户对应折扣
        //VipSetup vipSetup = vipSetupService.getOne(Wrappers.<VipSetup>lambdaQuery().eq(VipSetup::getVipLevel, userWechat.getVipLevel()));

        String orderNo = IdUtil.getSnowflake(1, 1).nextIdStr();
        Duration between = LocalDateTimeUtil.between(req.getStartTime(), req.getEndTime());
        BigDecimal twoDecimal = new BigDecimal(between.toMinutes());
        BigDecimal hour = twoDecimal.divide(BigDecimal.valueOf(60),2, RoundingMode.HALF_UP);
        BigDecimal payAmount = storeBox.getHourlyPrice().multiply(hour);
        //BigDecimal discountAmount = payAmount.multiply(vipSetup.getDiscountScale().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        // 扣减折扣
        //payAmount = payAmount.subtract(discountAmount);
        if (hour.compareTo(new BigDecimal(storeBox.getMinHours())) < 0 && (req.getRenew() == null || req.getRenew() == 0)) {
            throw new BusinessException(SystemError.ORDER_2001, storeBox.getMinHours());
        }
        PayInfoDto payInfo = new PayInfoDto();
        payInfo.setBody(store.getName()+"-"+storeBox.getName());
        payInfo.setPayAmount(payAmount.toBigInteger().doubleValue());
        //payInfo.setPayAmount(1.00);
        payInfo.setPayNo(orderNo);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        //组装对应支付参数
        orderRequest.setBody(payInfo.getBody());
        orderRequest.setOutTradeNo(payInfo.getPayNo());
        orderRequest.setTotalFee((int) payInfo.getPayAmount().doubleValue() * 100);
        //本机IP，统一下单接口必穿
        orderRequest.setSpbillCreateIp(wxPayConfiguration.getProperties().getSpbillCreateIp());
        orderRequest.setNotifyUrl(wxPayConfiguration.getProperties().getNotifyUrl());
        orderRequest.setTradeType("JSAPI");
        //设置openid
        orderRequest.setOpenid(userWechat.getOpenid());
        req.setPaymentAmount(BigDecimal.valueOf(payInfo.getPayAmount()));
        JSONObject attach = new JSONObject();
        // 1=门店订单
        attach.put("sid", req.getStoreId());
        attach.put("bid", req.getBoxId());
        attach.put("st", req.getStartTime());
        attach.put("et", req.getEndTime());
        attach.put("type", "1");
        attach.put("uid", userWechat.getId());
        attach.put("at", payAmount);
        orderRequest.setAttach(JSONObject.toJSONString(attach));
        WxPayMpOrderResult wxPayMpOrderResult = null;
        try {
            wxPayMpOrderResult = wxService.createOrder(orderRequest);
        } catch (WxPayException e){
            //后续插入支付情况与支付记录
            e.printStackTrace();
            Result result = new Result();
            result.setCode(500);
            result.setMsg(e.getErrCodeDes());
            return Result.failed(result);
        }

        return Result.success(wxPayMpOrderResult);
    }

    @ApiOperation(value = "充值会员付款")
    @PostMapping("/payment/vip")
    public Result<WxPayMpOrderResult> vip(@RequestBody @Validated PayVipReq req){

        String orderNo = IdUtil.getSnowflake(1, 1).nextIdStr();

        VipSetup vipSetup = vipSetupService.getOne(Wrappers.<VipSetup>lambdaQuery().eq(VipSetup::getVipLevel, req.getVipLevel()));

        PayInfoDto payInfo = new PayInfoDto();
        String pname = req.getVipLevel().equals("1") ? "会员" : "合伙人";
        payInfo.setBody(pname + "充值");
        payInfo.setPayAmount(vipSetup.getRechargeAmount().doubleValue());
        //payInfo.setPayAmount(1.00);
        payInfo.setPayNo(orderNo);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        //组装对应支付参数
        orderRequest.setBody(payInfo.getBody());
        orderRequest.setOutTradeNo(payInfo.getPayNo());
        orderRequest.setTotalFee((int) payInfo.getPayAmount().doubleValue() * 100);
        //本机IP，统一下单接口必穿
        orderRequest.setSpbillCreateIp(wxPayConfiguration.getProperties().getSpbillCreateIp());
        orderRequest.setNotifyUrl(wxPayConfiguration.getProperties().getNotifyUrl());
        orderRequest.setTradeType("JSAPI");
        //设置openid
        orderRequest.setOpenid(getUser().getOpenid());
        JSONObject attach = JSONObject.parseObject(JSONObject.toJSONString(req));
        // 2=会员
        attach.put("type", "2");
        attach.put("uid", getUser().getId());
        attach.put("ra", payInfo.getPayAmount());
        orderRequest.setAttach(JSONObject.toJSONString(attach));
        WxPayMpOrderResult wxPayMpOrderResult = null;
        try {
            wxPayMpOrderResult = wxService.createOrder(orderRequest);
        } catch (WxPayException e){
            e.printStackTrace();
            Result result = new Result();
            result.setCode(500);
            result.setMsg(e.getErrCodeDes());
            return Result.failed(result);
        }

        return Result.success(wxPayMpOrderResult);
    }

    @ApiOperation(value = "退款")
    @PostMapping("/refund/{id}")
    public Result refund(@PathVariable Integer id) throws WxPayException {
        Order order = orderService.getById(id);
        if (order == null) {
            throw new BusinessException(SystemError.ORDER_2404);
        }
        if (!order.getUserId().equals(getUser().getId())) {
            throw new BusinessException(SystemError.ORDER_2408);
        }
        if (order.getStartTime().compareTo(LocalDateTime.now()) < 0) {
            throw new BusinessException(SystemError.ORDER_2407);
        }
        if (order.getPayment() == 1) {
            // 微信支付的订单
            String refundNo = IdUtil.getSnowflake(1, 1).nextIdStr();

            WxPayRefundRequest request = new WxPayRefundRequest();
            request.setOutTradeNo(order.getOrderNo());
            request.setOutRefundNo(refundNo);
            request.setNotifyUrl(REFUNDURL);
            request.setTotalFee(order.getPaymentAmount().multiply(BigDecimal.valueOf(100)).intValue());
            request.setRefundFee(order.getPaymentAmount().multiply(BigDecimal.valueOf(100)).intValue());
            WxPayRefundResult result = this.wxService.refund(request);
            if (result.getResultCode().equals("SUCCESS")) {
                log.info("用户{},订单{},金额{}退款成功", order.getUserId(), order.getId(), order.getPaymentAmount());
            }
        } else {
            // 钱包支付的订单
            // 钱包加回对应订单金额
            Balance balance = new Balance();
            balance.setRechargeBalance(balance.getRechargeBalance().add(order.getPaymentAmount()));
            balanceService.update(balance, Wrappers.<Balance>lambdaQuery().eq(Balance::getUserId, order.getUserId()));
            // 订单状态修改
            order.setPayStatus(2);
            orderService.updateById(order);
        }
        return Result.success();
    }

    @ApiOperation(value = "退款回调")
    @PostMapping("/notify/refund")
    public Object notifyRefund(@RequestBody String xmlData) throws WxPayException {
        WxPayRefundNotifyResult result = this.wxService.parseRefundNotifyResult(xmlData);
        Order order = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, result.getReqInfo().getOutTradeNo()));
        order.setPayStatus(2);
        order.setRefundNo(result.getReqInfo().getOutRefundNo());
        orderService.updateById(order);
        return WxPayNotifyResponse.success("成功");
    }

    @ApiOperation(value = "支付回调")
    @PostMapping("/notify")
    public Object notify(@RequestBody String xmlData) throws WxPayException {
        log.info("支付成功回调通知，回调参数:{}", xmlData);
        WxPayOrderNotifyResult parseOrderNotifyResult = wxMiniPayService.parseOrderNotifyResult(xmlData);

        String attach = parseOrderNotifyResult.getAttach();
        String orderNo = parseOrderNotifyResult.getOutTradeNo();
        String bizPayNo = parseOrderNotifyResult.getTransactionId();

        JSONObject json = JSONObject.parseObject(attach);
        if (json.getString("type").equals("1")) {
            Order order = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, orderNo));
            if (order != null) {
                log.info("门店订单{}已存在", orderNo);
                return ResponseEntity.ok().build();
            }
            // 预约包厢订单
            OrderReq req = new OrderReq();
            req.setStoreId(json.getInteger("sid"));
            req.setBoxId(json.getInteger("bid"));
            req.setStartTime(LocalDateTimeUtil.parse(json.getString("st"), "yyyy-MM-dd HH:mm:ss"));
            req.setEndTime(LocalDateTimeUtil.parse(json.getString("et"), "yyyy-MM-dd HH:mm:ss"));
            req.setOrderNo(orderNo);
            req.setUserId(json.getInteger("uid"));
            req.setPayment(1);
            req.setPaymentAmount(json.getBigDecimal("at"));
            // 支付成功后再创建订单
            orderService.paySuccess(req);

        } else if (json.getString("type").equals("2")) {
            RechargeRecord record = rechargeRecordService.getOne(Wrappers.<RechargeRecord>lambdaQuery().eq(RechargeRecord::getOrderNo, orderNo));
            if (record != null) {
                log.info("充值订单{}已存在", orderNo);
                return ResponseEntity.ok().build();
            }
            // 会员充值记录
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setOrderNo(orderNo);
            rechargeRecord.setUserId(json.getInteger("uid"));
            rechargeRecord.setRechargeVipLevel(json.getInteger("vipLevel"));
            rechargeRecord.setRechargeAmount(json.getBigDecimal("ra"));
            rechargeRecord.setRechargeTime(LocalDateTime.now());
            rechargeRecordService.save(rechargeRecord);

            Balance balance = balanceService.getOne(Wrappers.<Balance>lambdaQuery().eq(Balance::getUserId, json.getInteger("uid")));
            // 更新充值余额
            if (balance != null) {
                balance.setRechargeBalance(balance.getRechargeBalance().add(json.getBigDecimal("ra")));
                balanceService.updateById(balance);
            } else {
                balance = new Balance();
                balance.setUserId(json.getInteger("uid"));
                balance.setRechargeBalance(json.getBigDecimal("ra"));
                balanceService.save(balance);
            }

            // 会员信息记录
            UserWechat userWechat = userWechatService.getById(json.getInteger("uid"));
            userWechat.setId(json.getInteger("uid"));
            userWechat.setVipLevel(json.getInteger("vipLevel"));
            userWechat.setVipOpening(userWechat.getVipOpening() != null ? userWechat.getVipOpening() : LocalDateTime.now());
            // 1年有效期,充值后自动加一年
            userWechat.setVipExpire(userWechat.getVipExpire() != null ? userWechat.getVipExpire().plusYears(1) : LocalDateTime.now().plusYears(1));

            userWechatService.updateById(userWechat);
        }

        return WxPayNotifyResponse.success("成功");
    }

    @ApiOperation(value = "订单列表")
    @PostMapping("/list")
    public Result<IPage<OrderListDTO>> list(@RequestBody OrderListReq req) {
        req.setUserId(getUser().getId());
        return Result.success(orderService.listPage(req));
    }

    @ApiOperation(value = "订单列表-管理后台使用")
    @PostMapping("")
    public Result<List<OrderListDTO>> listAdmin(@RequestBody OrderListReq req) {
        List<Order> orders = orderService.list(Wrappers.<Order>lambdaQuery()
                .like(StringUtils.isNotBlank(req.getOrderNo()), Order::getOrderNo, req.getOrderNo())
                .like(null != req.getStoreName(), Order::getStoreName, req.getStoreName())
                .like(null != req.getBoxName(), Order::getBoxName, req.getBoxName())
                .orderByDesc(Order::getCreateTime)
        );
        List<OrderListDTO> result = BeanUtil.copyToList(orders, OrderListDTO.class);
        result.forEach(dto -> {
            UserWechat userWechat = userWechatService.getById(dto.getUserId());
            dto.setNickName(userWechat.getNickName());
        });
        return Result.success(result);
    }

    @ApiOperation(value = "订单详情")
    @GetMapping("/{id}")
    public Result<OrderDetailDTO> detail(@PathVariable Integer id) {
        return Result.success(orderService.detail(id, getUser().getNickName()));
    }
}
