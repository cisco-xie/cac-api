package com.geekuniverse.cac.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.result.Result;
import com.geekuniverse.cac.dto.WithdrawalApplicationDTO;
import com.geekuniverse.cac.entity.UserWechat;
import com.geekuniverse.cac.entity.VipSetup;
import com.geekuniverse.cac.entity.WithdrawalApplication;
import com.geekuniverse.cac.req.VipSetupReq;
import com.geekuniverse.cac.service.IVipSetupService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 会员充值字典表 前端控制器
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-07-22
 */
@RestController
@RequestMapping("/vip/rules")
public class VipSetupController {

    @Resource
    private IVipSetupService vipSetupService;

    @GetMapping("")
    @ApiOperation(value = "规则列表", notes = "会员规则列表")
    public Result<List<VipSetup>> applications() {
        List<VipSetup> vipSetup = vipSetupService.list();
        return Result.success(vipSetup);
    }

    @PutMapping("")
    @ApiOperation(value = "修改", notes = "修改会员规则")
    public Result edit(@RequestBody @Validated VipSetupReq req) {
        VipSetup vipSetup = vipSetupService.getById(req.getId());
        if (vipSetup.getVipLevel() == 2 && req.getIncomeScale() == null) {
            throw new BusinessException(SystemError.USER_VIP_1100);
        }
        vipSetup.setRechargeAmount(req.getRechargeAmount());
        vipSetup.setDiscountScale(req.getDiscountScale());
        vipSetup.setIncomeScale(req.getIncomeScale());
        vipSetupService.updateById(vipSetup);
        return Result.success();
    }

}
