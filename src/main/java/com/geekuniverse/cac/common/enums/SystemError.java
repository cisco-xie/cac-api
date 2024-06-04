package com.geekuniverse.cac.common.enums;

import com.geekuniverse.cac.common.constants.ErrCode;

/**
 * @author 谢诗宏
 * @description 统一异常码
 * @date 2022/12/10
 */
public enum SystemError implements ErrCode {

    /**
     * 基础异常
     */
    SYS_500(500, "服务器繁忙,请稍后重试！"),
    SYS_502(502, "系统维护中"),
    SYS_503(503, "未找到[%s]微服务,请检查服务是否可用"),
    SYS_504(504, "未找到微服务"),

    SYS_400(400, "错误的请求"),
    SYS_401(401, "没有权限"),
    SYS_402(402, "参数不完整"),
    SYS_404(404, "Not Found"),

    SYS_409(409, "缺少请求参数[%s]"),
    SYS_410(410, "请求方式错误"),
    SYS_411(411, "当前数据不存在"),
    SYS_418(418, "请求参数[%s]格式错误"),

    /**
     * 用户相关异常--1000起
     */
    USER_1000(1000, "未登录"),
    USER_1001(1001, "没有权限"),
    USER_1002(1002, "用户不存在"),
    USER_1003(1003, "token错误"),
    USER_1004(1004, "登录过期"),
    USER_1005(1005, "微信登录异常"),
    USER_1006(1006, "密码错误"),

    USER_VIP_1100(1100, "合伙人盈利比率不能为空"),

    /**
     * 订单相关异常--2000起
     */
    ORDER_2000(2000, "所选时间和他人预约时间冲突！请重新选择预约时间"),
    ORDER_2001(2001, "预约时间必须大于%s小时"),
    ORDER_2002(2002, "钱包余额不足"),
    ORDER_2003(2003, "续费订单必须从原订单的结束时间开始"),
    ORDER_2404(2404, "订单不存在"),
    ORDER_2405(2405, "订单门店不存在"),
    ORDER_2406(2406, "订单已失效，请重新下单预约"),
    ORDER_2407(2407, "订单已开始无法申请退款"),
    ORDER_2408(2408, "该订单不属于当前用户"),
    ORDER_2409(2409, "订单只可提前30分钟开门,请耐心等待"),

    ORDER_WITHDRAWAL_2500(2500, "可提现余额不足"),

    /**
     * 设备相关异常--3000起
     */
    DEVICE_3000(3000, "开门失败！请稍后重试"),
    DEVICE_3001(3001, "插座启动失败！请稍后重试"),

    STORE_4000(4000, "暂不支持直接创建门店！需联系开发者服务商"),
    ;

    private final Integer code;
    private final String msg;

    SystemError(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public static SystemError getDefined(Integer code) {
        for (SystemError err : SystemError.values()) {
            if (err.code.equals(code)) {
                return err;
            }
        }
        return SystemError.SYS_500;
    }

}
