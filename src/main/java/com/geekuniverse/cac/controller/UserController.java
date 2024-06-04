package com.geekuniverse.cac.controller;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.result.Result;
import com.geekuniverse.cac.core.support.BaseController;
import com.geekuniverse.cac.dto.UserListAdminDTO;
import com.geekuniverse.cac.entity.Balance;
import com.geekuniverse.cac.entity.IncomeRecord;
import com.geekuniverse.cac.entity.UserWechat;
import com.geekuniverse.cac.entity.WithdrawalApplication;
import com.geekuniverse.cac.req.UserEditReq;
import com.geekuniverse.cac.req.WxLoginReq;
import com.geekuniverse.cac.service.IBalanceService;
import com.geekuniverse.cac.service.IIncomeRecordService;
import com.geekuniverse.cac.service.IUserWechatService;
import com.geekuniverse.cac.service.IWithdrawalApplicationService;
import com.geekuniverse.cac.thirdparty.wechat.api.WeChatApi;
import com.geekuniverse.cac.thirdparty.wechat.response.UserSessionDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户相关 前端控制器
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-01-05
 */
@Slf4j
@Api(tags = "用户模块")
@RestController
//@AllArgsConstructor
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private IUserWechatService userWechatService;

    @Resource
    private IIncomeRecordService incomeRecordService;

    @Resource
    private WxMaService wxMaService;

    @Resource
    private IBalanceService balanceService;

    @Resource
    private IWithdrawalApplicationService withdrawalApplicationService;

    @Value("${thirdparty.wechat.miniapp.configs[0].appid}")
    private String appId;

    @GetMapping("")
    public Result<List<UserListAdminDTO>> list() {
        List<UserWechat> userWechats = userWechatService.list(Wrappers.<UserWechat>lambdaQuery().orderByDesc(UserWechat::getCreateTime));
        List<UserListAdminDTO> result = BeanUtil.copyToList(userWechats, UserListAdminDTO.class);
        result.forEach(user -> {
            // 获取相关余额
            Balance balance = balanceService.getOne(Wrappers.<Balance>lambdaQuery().eq(Balance::getUserId, user.getId()));
            if (balance != null) {
                user.setIncomeBalance(balance.getIncomeBalance());
                user.setRechargeBalance(balance.getRechargeBalance());
            }
        });
        return Result.success(result);
    }

    @GetMapping("/application/withdrawal")
    @ApiOperation(value = "提现申请", notes = "用户提现申请接口")
    @ApiImplicitParams
    ({
            @ApiImplicitParam(name = "amount", value = "提现金额", required = true, dataType = "int", dataTypeClass = BigDecimal.class),
            @ApiImplicitParam(name = "qrcode", value = "收款二维码地址", required = true, dataType = "string", dataTypeClass = String.class)
    })
    public Result application(@RequestParam BigDecimal amount, @RequestParam String qrcode) {
        Balance balance = balanceService.getOne(Wrappers.<Balance>lambdaQuery().eq(Balance::getUserId, getUser().getId()));
        if (balance == null) {
            throw new BusinessException(SystemError.ORDER_WITHDRAWAL_2500);
        } else {
            if (balance.getIncomeBalance().compareTo(amount) < 0) {
                throw new BusinessException(SystemError.ORDER_WITHDRAWAL_2500);
            }
            WithdrawalApplication withdrawalApplication = new WithdrawalApplication();
            withdrawalApplication.setUserId(balance.getUserId());
            withdrawalApplication.setWithdrawalAmount(amount);
            withdrawalApplication.setPaymentCode(qrcode);
            withdrawalApplicationService.save(withdrawalApplication);

            balance.setIncomeBalance(balance.getIncomeBalance().subtract(amount));
            balanceService.updateById(balance);
        }
        return Result.success();
    }

    @PostMapping("/wx/login")
    @ApiOperation(value = "通过微信code进行登录", notes = "通过微信code获取openid进行静默登录")
    public Result<UserWechat> wxlogin(@RequestBody @Validated WxLoginReq req) {

        if (!wxMaService.switchover(appId)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appId));
        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(req.getCode());
            log.info(session.getSessionKey());
            log.info(session.getOpenid());
            UserSessionDTO userSession = BeanUtil.copyProperties(session, UserSessionDTO.class);
            // 登录
            UserWechat userWechat = userWechatService.login(getResponse(), userSession);
            if (userWechat.getPhone() == null) {
                WxMaPhoneNumberInfo phoneNumberInfo = wxMaService.getUserService().getPhoneNoInfo(session.getSessionKey(), req.getEncryptedData(), req.getIv());
                if (phoneNumberInfo != null) {
                    userWechat.setPhone(phoneNumberInfo.getPhoneNumber());
                }
                userWechatService.updateById(userWechat);
            }

            // 获取相关余额
            Balance balance = balanceService.getOne(Wrappers.<Balance>lambdaQuery().eq(Balance::getUserId, userWechat.getId()));
            if (balance != null) {
                userWechat.setIncomeBalance(balance.getIncomeBalance());
                userWechat.setRechargeBalance(balance.getRechargeBalance());
            }
            QueryWrapper<WithdrawalApplication> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("ifnull(sum(withdrawal_amount),0) as withdrawalAmount");
            queryWrapper.eq("user_id", userWechat.getId());
            queryWrapper.ne("status", 3);
            WithdrawalApplication withdrawalApplication = withdrawalApplicationService.getOne(queryWrapper);
            QueryWrapper<WithdrawalApplication> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select("ifnull(sum(withdrawal_amount),0) as withdrawalAmount");
            queryWrapper2.eq("user_id", userWechat.getId());
            queryWrapper2.eq("status", 3);
            WithdrawalApplication withdrawalApplication2 = withdrawalApplicationService.getOne(queryWrapper2);
            userWechat.setWithdrawingBalance(withdrawalApplication.getWithdrawalAmount());
            userWechat.setWithdrawnBalance(withdrawalApplication2.getWithdrawalAmount());

            return Result.success(userWechat);
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return Result.failed(e.getError().getErrorCode(), e.getError().getErrorMsg());
        } finally {
            //清理ThreadLocal
            WxMaConfigHolder.remove();
        }
    }

    @GetMapping("/wx/info")
    @ApiOperation(value = "获取微信用户信息", notes = "获取微信用户信息")
    public Result<WxMaUserInfo> info(String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        if (!wxMaService.switchover(appId)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appId));
        }

        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            WxMaConfigHolder.remove();//清理ThreadLocal
            return Result.failed(500, "用户校验失败");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        WxMaConfigHolder.remove();//清理ThreadLocal
        //userWechatService.updateUserInfo(userInfo);
        return Result.success(userInfo);
    }

    @GetMapping("/wx/phone")
    public Result<WxMaPhoneNumberInfo> phone(String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        if (!wxMaService.switchover(appId)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appId));
        }

        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            WxMaConfigHolder.remove();//清理ThreadLocal
            return Result.failed(500, "用户校验失败");
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
        WxMaConfigHolder.remove();//清理ThreadLocal
        return Result.success(phoneNoInfo);
    }

    @GetMapping("/wx/share/qrcode")
    @ApiOperation(value = "获取小程序码", notes = "获取小程序码")
    public Result<Object> qrcode() throws WxErrorException {
        byte[] qrcode = wxMaService.getQrcodeService().createWxaCodeUnlimitBytes(String.valueOf(getUser().getId()), "page/index/index", false, "release", 430, true, null, true);
        WxMaConfigHolder.remove();//清理ThreadLocal
        return Result.success(qrcode);
    }

    @PutMapping("")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    public Result update(@RequestBody @Validated UserEditReq req) {
        userWechatService.update(req);
        return Result.success();
    }

}
