package com.geekuniverse.cac.controller;

import com.geekuniverse.cac.core.result.Result;
import com.geekuniverse.cac.core.support.BaseController;
import com.geekuniverse.cac.dto.UserAdminDTO;
import com.geekuniverse.cac.entity.UserAdmin;
import com.geekuniverse.cac.req.AdminLoginReq;
import com.geekuniverse.cac.service.IUserAdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/user")
public class UserAdminController extends BaseController {

    @Resource
    private IUserAdminService userAdminService;

    @PostMapping("/login")
    public Result<UserAdminDTO> login(@RequestBody @Validated AdminLoginReq req) {
        return Result.success(userAdminService.login(getResponse(), req));
    }

}
