package com.geekuniverse.cac.thirdparty.unisoft.api;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.geekuniverse.cac.thirdparty.unisoft.response.DeviceListDTO;
import com.geekuniverse.cac.thirdparty.wechat.api.WeChatApi;
import com.geekuniverse.cac.thirdparty.wechat.response.UserSessionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;


/**
 * 统软云API对接
 *
 * @name: UnisoftApi
 * @author: 谢诗宏
 * @date: 2023-05-15 18:27
 **/
@Component
public class UnisoftApi {

    public static String APPID;
    @Value("${thirdparty.unisoft.appid}")
    public void setAPPID(String APPID) {
        UnisoftApi.APPID = APPID;
    }

    public static String PASSWORD;
    @Value("${thirdparty.unisoft.password}")
    public void setPASSWORD(String PASSWORD) {
        UnisoftApi.PASSWORD = PASSWORD;
    }

    public static String BASE_URL;
    @Value("${thirdparty.unisoft.base-url}")
    public void setBaseUrl(String baseUrl) {
        UnisoftApi.BASE_URL = baseUrl;
    }

    public static String LIST;
    @Value("${thirdparty.unisoft.api.list}")
    public void setLIST(String LIST) {
        UnisoftApi.LIST = LIST;
    }

    public static String CONTROL;
    @Value("${thirdparty.unisoft.api.control}")
    public void setControl(String CONTROL) {
        UnisoftApi.CONTROL = CONTROL;
    }


    /**
     * 获取设备列表
     *
     * @return {@link DeviceListDTO}
     */
    public static JSONObject getDeviceList() {
        long timestamp = new Date().getTime() / 1000L;
        String sign = SecureUtil.md5(SecureUtil.md5(PASSWORD)+timestamp);
        String url = BASE_URL + LIST + "?sign=" + sign + "&ts=" + timestamp;
        String resultStr = HttpUtil.get(url);
        return JSONObject.parseObject(resultStr);
    }

    /**
     * 向设备下发指令
     *
     * @param device 设备
     * @param power  开关 0关/1开
     * @param reset  延时重启 单位ms
     */
    public static JSONObject control(String device, int power, long reset) {
        long timestamp = new Date().getTime() / 1000L;
        String sign = SecureUtil.md5(SecureUtil.md5(PASSWORD)+timestamp);
        String url = BASE_URL + CONTROL + "?sign=" + sign + "&ts=" + timestamp;
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("device", device);
        paramMap.put("power", power);
        paramMap.put("reset", reset == 0L ? null : reset);
        String jsonParams = JSON.toJSONString(paramMap);
        String resultStr = HttpUtil.post(url, jsonParams);
        return JSONObject.parseObject(resultStr);
    }

}
