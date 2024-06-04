package com.geekuniverse.cac.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.constants.Constants;
import com.geekuniverse.cac.common.constants.RedisConstants;
import com.geekuniverse.cac.common.utils.KeyUtil;
import com.geekuniverse.cac.common.utils.TokenUtil;
import com.geekuniverse.cac.core.model.TokenUser;
import com.geekuniverse.cac.entity.StoreBox;
import com.geekuniverse.cac.entity.UserWechat;
import com.geekuniverse.cac.mapper.UserWechatMapper;
import com.geekuniverse.cac.req.UserEditReq;
import com.geekuniverse.cac.service.IUserWechatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geekuniverse.cac.thirdparty.wechat.response.UserSessionDTO;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 微信用户表 服务实现类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-15
 */
@Service
public class UserWechatServiceImpl extends ServiceImpl<UserWechatMapper, UserWechat> implements IUserWechatService {

    @Value("${auth.user.token.timeout.app}")
    private Long tokenTimeoutApp;

    @Resource
    private RedissonClient redisson;

    @Override
    public UserWechat login(HttpServletResponse response, UserSessionDTO userSession) {
        UserWechat userWechat = baseMapper.selectOne(Wrappers.<UserWechat>lambdaQuery()
                .eq(UserWechat::getOpenid, userSession.getOpenid()));
        if (userWechat == null) {
            userWechat = BeanUtil.copyProperties(userSession, UserWechat.class);
            baseMapper.insert(userWechat);
        }

        TokenUser tokenUser = BeanUtil.copyProperties(userWechat, TokenUser.class);
        tokenUser.setLastLoginTime(LocalDateTime.now());

        String token = TokenUtil.create(tokenUser);
        response.addHeader(Constants.JWT_TOKEN_HEADER, token);
        redisson.getBucket(KeyUtil.genKey(RedisConstants.JWT_TOKEN_PREFIX, String.valueOf(tokenUser.getId()))).set(token, tokenTimeoutApp, TimeUnit.DAYS);

        return userWechat;
    }

    @Override
    public void updateUserInfo(WxMaUserInfo wxMaUserInfo) {
        UserWechat userWechat = baseMapper.selectOne(Wrappers.<UserWechat>lambdaQuery()
                .eq(UserWechat::getUnionid, wxMaUserInfo.getUnionId()));
        if (userWechat != null) {
            userWechat = BeanUtil.copyProperties(wxMaUserInfo, UserWechat.class);
            baseMapper.updateById(userWechat);
        }
    }

    @Override
    public void update(UserEditReq req) {
        UserWechat userWechat = BeanUtil.copyProperties(req, UserWechat.class);
        baseMapper.update(userWechat, Wrappers.<UserWechat>lambdaQuery()
                .eq(UserWechat::getOpenid, req.getOpenid()));
    }
}
