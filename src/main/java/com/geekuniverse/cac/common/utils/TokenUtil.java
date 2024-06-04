package com.geekuniverse.cac.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.geekuniverse.cac.core.model.TokenUser;

import java.util.Map;

/**
 * @author 谢诗宏
 * @description token工具类 - jwt
 * @date 2022/11/24
 */
public class TokenUtil {
    // 密钥
    private static final byte[] KEY = "dance-data-2022".getBytes();

    public static String create(TokenUser tokenUser) {
        Map<String, Object> map = BeanUtil.beanToMap(tokenUser);
        return JWTUtil.createToken(map, KEY);
    }

    public static TokenUser parse(String token) {
        final JWT jwt = JWTUtil.parseToken(token);
        //return JSON.parseObject(JSON.toJSONString(String.valueOf(jwt.getPayload())), TokenUser.class);
        return BeanUtil.toBean(jwt.getPayload().getClaimsJson(), TokenUser.class);
    }

    public static boolean verify(String token) {
        return JWTUtil.verify(token, KEY);
    }

}
