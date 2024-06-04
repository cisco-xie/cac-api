package com.geekuniverse.cac.service;

import com.geekuniverse.cac.dto.UserAdminDTO;
import com.geekuniverse.cac.entity.UserAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geekuniverse.cac.req.AdminLoginReq;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-25
 */
public interface IUserAdminService extends IService<UserAdmin> {

    /**
     * 管理后台登录
     * @param req
     * @return
     */
    UserAdminDTO login(HttpServletResponse response, AdminLoginReq req);
}
