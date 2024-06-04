package com.geekuniverse.cac.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "thirdparty.wechat.miniapp")
public class WxProperties {

    private List<Config> configs;

    /**
     * 司机订阅消息模板id
     */
    private String driverTemplateId;

    /**
     * 管理端装载通知模板id
     */
    private String loadTemplateId;

    /**
     * 管理端签收通知模板id
     */
    private String unLoadTemplateId;

    @Data
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String msgDataFormat;

        /**
         * 小程序名称
         */
        private String name;

    }
}