package com.geekuniverse.cac.service;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.geekuniverse.cac.entity.UserWechat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geekuniverse.cac.req.UserEditReq;
import com.geekuniverse.cac.thirdparty.wechat.response.UserSessionDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 微信用户表 服务类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
public interface IUserWechatService extends IService<UserWechat> {

    UserWechat login(HttpServletResponse response, UserSessionDTO userSession);

    void updateUserInfo(WxMaUserInfo wxMaUserInfo);

    void update(UserEditReq req);

}
