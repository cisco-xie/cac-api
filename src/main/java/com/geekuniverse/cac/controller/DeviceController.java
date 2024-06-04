package com.geekuniverse.cac.controller;

import com.alibaba.fastjson2.JSONObject;
import com.geekuniverse.cac.common.constants.Constants;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.result.Result;
import com.geekuniverse.cac.core.support.BaseController;
import com.geekuniverse.cac.entity.Order;
import com.geekuniverse.cac.entity.StoreBox;
import com.geekuniverse.cac.service.IOrderService;
import com.geekuniverse.cac.service.IStoreBoxService;
import com.geekuniverse.cac.thirdparty.unisoft.api.UnisoftApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备 前端控制器
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Slf4j
@Api(tags = "设备模块")
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {

    @Resource
    private IStoreBoxService storeBoxService;

    @Resource
    private IOrderService orderService;

    @ApiOperation(value = "开门")
    @PostMapping("/{id}/{status}")
    @ApiImplicitParams
    ({
            @ApiImplicitParam(name = "id", value = "订单ID", required = true, dataType = "int", paramType = "path", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "status", value = "状态1开门0离店", required = true, dataType = "int", paramType = "path", dataTypeClass = Integer.class)
    })
    public Result list(@PathVariable Integer id, @PathVariable Integer status) {
        Order order = orderService.getById(id);
        LocalDateTime now = LocalDateTime.now();

        if (order.getStartTime().isAfter(now) && order.getStartTime().isAfter(now.plusMinutes(30))) {
            throw new BusinessException(SystemError.ORDER_2409);
        }
        if (order.getEndTime().isBefore(now)) {
            throw new BusinessException(SystemError.ORDER_2406);
        }
        if (order.getPayStatus() == 2) {
            throw new BusinessException(SystemError.ORDER_2406);
        }
        StoreBox storeBox = storeBoxService.getById(order.getBoxId());
        if (storeBox == null) {
            throw new BusinessException(SystemError.ORDER_2405);
        }
        int socket10a = Constants.DevicePower.OPEN.getValue();
        int socket16a = Constants.DevicePower.OPEN.getValue();
        if (status == 0) {
            socket10a = Constants.DevicePower.OPEN.getValue();
            socket16a = Constants.DevicePower.CLOSE.getValue();
        } else if (status == 1) {
            socket10a = Constants.DevicePower.CLOSE.getValue();
            socket16a = Constants.DevicePower.OPEN.getValue();
        }

        /** 包厢设备控制 0000表示没有门禁设备 start */
        if (!"0000".equals(storeBox.getDeviceSocket10a()) && StringUtils.isNotBlank(storeBox.getDeviceSocket10a())) {
            // 第一步：关闭出门开关，用于断电开门，并在10秒后延时打开
            JSONObject result = UnisoftApi.control(storeBox.getDeviceSocket10a(), socket10a, 10000L);
            if (200 == result.getInteger("code")) {
                log.info("开门成功：用户ID[{}],订单ID[{}]", order.getUserId(), id);
            } else {
                log.info("开门失败:用户ID[{}],订单ID[{}],异常信息[{}]", order.getUserId(), id, result);
                throw new BusinessException(SystemError.SYS_500);
            }
        }

        // 第二步：打开墙壁插座
        if (StringUtils.isNotBlank(storeBox.getDeviceSocket16a())) {
            JSONObject resultSocket = UnisoftApi.control(storeBox.getDeviceSocket16a(), socket16a, 0L);
            if (200 == resultSocket.getInteger("code")) {
                log.info("打开墙壁插座成功：用户ID[{}],订单ID[{}]", order.getUserId(), id);
            } else {
                log.info("打开墙壁插座失败:用户ID[{}],订单ID[{}],异常信息[{}]", order.getUserId(), id, resultSocket);
                throw new BusinessException(SystemError.SYS_500);
            }
        }
        /** 包厢设备控制 end */
        return Result.success();
    }

}
