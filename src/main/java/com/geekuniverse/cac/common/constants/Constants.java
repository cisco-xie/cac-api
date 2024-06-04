package com.geekuniverse.cac.common.constants;

import lombok.Getter;

/**
 * @author 谢诗宏
 * @description 基础常量
 * @date 2022/11/16
 */
public class Constants {

    /**
     * 认证信息Http请求头
     */
    public static final String JWT_TOKEN_HEADER = "authorization";

    /**
     * 令牌前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * 分隔符
     */
    public static class Separator {
        /* 默认分隔符 */
        public static final String DEFAULT = ",";
        public static final String VERTICAL = "\\|";
        public static final String COMMA = ",";
        public static final String BAR = "-";
        public static final String COLON = ":";
        public static final String UNDERLINE = "_";
        public static final String QUESTION = "?";
        public static final String SEMICOLON = ";";
        public static final String EQUAL = "=";
        public static final String LIKE = "*";
        public static final String AND = "&";
    }

    /**
     * 语言
     */
    public enum Language {
        ZH_CN,
        EN_US,
    }

    /**
     * 直播状态
     */
    @Getter
    public enum LiveStatus {

        // 在线
        ONLINE(1),
        // 离线
        OFFLINE(0),
        ;

        private final int value;

        LiveStatus(int value) {
            this.value = value;
        }
    }

    /**
     * 设备电源状态
     */
    @Getter
    public enum DevicePower {

        // 打开
        OPEN(1),
        // 关闭
        CLOSE(0),
        ;

        private final int value;

        DevicePower(int value) {
            this.value = value;
        }
    }

}
