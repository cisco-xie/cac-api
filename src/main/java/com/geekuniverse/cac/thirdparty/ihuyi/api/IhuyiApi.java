package com.geekuniverse.cac.thirdparty.ihuyi.api;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.geekuniverse.cac.thirdparty.unisoft.response.DeviceListDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;


/**
 * 短信API对接
 *
 * @name: IhuyiApi
 * @author: 谢诗宏
 * @date: 2023-05-15 18:27
 **/
@Component
public class IhuyiApi {

    public static String APPID;
    @Value("${thirdparty.ihuyi.appid}")
    public void setAPPID(String APPID) {
        IhuyiApi.APPID = APPID;
    }

    public static String APIKEY;
    @Value("${thirdparty.ihuyi.apikey}")
    public void setAPIKEY(String APIKEY) {
        IhuyiApi.APIKEY = APIKEY;
    }

    public static String CONTEXT;
    @Value("${thirdparty.ihuyi.context}")
    public void setCONTEXT(String CONTEXT) {
        IhuyiApi.CONTEXT = CONTEXT;
    }

    public static String BASE_URL;
    @Value("${thirdparty.ihuyi.base-url}")
    public void setBaseUrl(String baseUrl) {
        IhuyiApi.BASE_URL = baseUrl;
    }

    public static String SMS;
    @Value("${thirdparty.ihuyi.api.sms}")
    public void setSMS(String SMS) {
        IhuyiApi.SMS = SMS;
    }

    public static String VOICE;
    @Value("${thirdparty.ihuyi.api.voice}")
    public void setVOICE(String VOICE) {
        IhuyiApi.VOICE = VOICE;
    }

    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param order  订单
     */
    public static String sms(String mobile, String order) throws UnsupportedEncodingException {
        CONTEXT = String.format(CONTEXT, order);

        String url = BASE_URL + SMS + "&account=" + APPID + "&password=" + APIKEY + "&mobile=" + mobile + "&content=" + CONTEXT;
        return HttpUtil.get(url);
    }

    /**
     * 发送语音
     *
     * @param mobile 手机号
     * @param order  订单
     */
    public static String voice(String mobile, String order) throws UnsupportedEncodingException {
        CONTEXT = String.format(CONTEXT, order);

        String url = BASE_URL + VOICE + "&account=" + APPID + "&password=" + APIKEY + "&mobile=" + mobile + "&content=" + CONTEXT;
        return HttpUtil.get(url);
    }

}
