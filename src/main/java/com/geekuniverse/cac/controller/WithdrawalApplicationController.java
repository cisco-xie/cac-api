package com.geekuniverse.cac.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.result.Result;
import com.geekuniverse.cac.dto.WithdrawalApplicationDTO;
import com.geekuniverse.cac.entity.Balance;
import com.geekuniverse.cac.entity.UserWechat;
import com.geekuniverse.cac.entity.WithdrawalApplication;
import com.geekuniverse.cac.service.IBalanceService;
import com.geekuniverse.cac.service.IUserWechatService;
import com.geekuniverse.cac.service.IWithdrawalApplicationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户提现申请表 前端控制器
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-07-20
 */
@RestController
@RequestMapping("/application/withdrawal")
public class WithdrawalApplicationController {

    @Resource
    private IUserWechatService userWechatService;

    @Resource
    private IWithdrawalApplicationService withdrawalApplicationService;

    @GetMapping("")
    @ApiOperation(value = "提现申请列表", notes = "用户提现申请列表接口")
    @ApiImplicitParams
            ({
                    @ApiImplicitParam(name = "amount", value = "提现金额", required = true, dataType = "int", dataTypeClass = BigDecimal.class),
                    @ApiImplicitParam(name = "qrcode", value = "收款二维码地址", required = true, dataType = "string", dataTypeClass = String.class)
            })
    public Result<List<WithdrawalApplicationDTO>> applications() {
        List<WithdrawalApplication> applications = withdrawalApplicationService.list(Wrappers.<WithdrawalApplication>lambdaQuery().orderByAsc(WithdrawalApplication::getStatus).orderByDesc(WithdrawalApplication::getCreateTime));
        List<WithdrawalApplicationDTO> result = BeanUtil.copyToList(applications, WithdrawalApplicationDTO.class);
        result.forEach(dto -> {
            UserWechat userWechat = userWechatService.getById(dto.getUserId());
            dto.setAvatarUrl(userWechat.getAvatarUrl());
            dto.setNickname(userWechat.getNickName());
            dto.setPhone(userWechat.getPhone());
        });
        return Result.success(result);
    }

    @PutMapping("/pass/{id}/{status}")
    @ApiOperation(value = "通过申请", notes = "通过申请")
    @ApiImplicitParams
            ({
                    @ApiImplicitParam(name = "id", value = "申请ID", required = true, dataType = "int", dataTypeClass = Integer.class),
                    @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "int", dataTypeClass = Integer.class)
            })
    public Result pass(@PathVariable Integer id, @PathVariable Integer status) {
        WithdrawalApplication app = new WithdrawalApplication();
        app.setId(id);
        app.setStatus(status);
        withdrawalApplicationService.updateById(app);
        return Result.success();
    }

}
