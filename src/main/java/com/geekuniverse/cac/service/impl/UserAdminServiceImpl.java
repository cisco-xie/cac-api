package com.geekuniverse.cac.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geekuniverse.cac.common.constants.Constants;
import com.geekuniverse.cac.common.constants.RedisConstants;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.common.utils.KeyUtil;
import com.geekuniverse.cac.common.utils.TokenUtil;
import com.geekuniverse.cac.core.exception.BusinessException;
import com.geekuniverse.cac.core.model.TokenUser;
import com.geekuniverse.cac.dto.UserAdminDTO;
import com.geekuniverse.cac.entity.UserAdmin;
import com.geekuniverse.cac.mapper.UserAdminMapper;
import com.geekuniverse.cac.req.AdminLoginReq;
import com.geekuniverse.cac.service.IUserAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 谢诗宏
 * @since 2023-05-25
 */
@Service
public class UserAdminServiceImpl extends ServiceImpl<UserAdminMapper, UserAdmin> implements IUserAdminService {

    @Value("${auth.user.token.timeout.app}")
    private Long tokenTimeoutApp;

    @Resource
    private RedissonClient redisson;

    @Override
    public UserAdminDTO login(HttpServletResponse response, AdminLoginReq req) {
        UserAdmin userAdmin = baseMapper.selectOne(Wrappers.<UserAdmin>lambdaQuery()
                .eq(UserAdmin::getAccount, req.getAccount())
        );
        if (userAdmin == null) {
            throw new BusinessException(SystemError.USER_1002);
        }
        if (!userAdmin.getPassword().equals(req.getPassword())) {
            throw new BusinessException(SystemError.USER_1006);
        }

        TokenUser tokenUser = BeanUtil.copyProperties(userAdmin, TokenUser.class);
        tokenUser.setLastLoginTime(LocalDateTime.now());

        String token = TokenUtil.create(tokenUser);
        response.addHeader(Constants.JWT_TOKEN_HEADER, token);
        redisson.getBucket(KeyUtil.genKey(RedisConstants.JWT_ADMIN_TOKEN_PREFIX, String.valueOf(tokenUser.getId()))).set(token, tokenTimeoutApp, TimeUnit.DAYS);
        UserAdminDTO result = BeanUtil.copyProperties(userAdmin, UserAdminDTO.class);
        result.setToken(token);
        result.setFailureTime(LocalDateTime.now().plusDays(30).toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
        return result;
    }

}
