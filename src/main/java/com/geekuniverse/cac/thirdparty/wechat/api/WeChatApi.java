package com.geekuniverse.cac.thirdparty.wechat.api;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.geekuniverse.cac.thirdparty.wechat.response.UserSessionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * 微信小程序API对接
 *
 * @name: WeChatApi
 * @author: 谢诗宏
 * @date: 2023-05-15 18:27
 **/
@Component
public class WeChatApi {

    /*public static String APPID;
    @Value("${thirdparty.wechat.appid}")
    public void setAPPID(String APPID) {
        WeChatApi.APPID = APPID;
    }

    public static String SECRET;
    @Value("${thirdparty.wechat.secret}")
    public void setSECRET(String SECRET) {
        WeChatApi.SECRET = SECRET;
    }

    public static String BASE_URL;
    @Value("${thirdparty.wechat.base-url}")
    public void setBaseUrl(String baseUrl) {
        WeChatApi.BASE_URL = baseUrl;
    }


    public static String CODE2_SESSION_URL;
    @Value("${thirdparty.wechat.code2Session-url}")
    public void setCode2SessionUrl(String code2SessionUrl) {
        WeChatApi.CODE2_SESSION_URL = code2SessionUrl;
    }

    public static UserSessionDTO jscode2session(String jsCode) {
        String url = BASE_URL + CODE2_SESSION_URL + "?appid=" + APPID + "&secret=" + SECRET + "&js_code=" + jsCode + "&grant_type=authorization_code";
        String resultStr = HttpUtil.get(url);
        UserSessionDTO dto = JSONObject.parseObject(resultStr, UserSessionDTO.class);
        return dto;
    }*/

}
