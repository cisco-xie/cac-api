package com.geekuniverse.cac.common.constants;

import lombok.Getter;

/**
 * @author 谢诗宏
 * @description FastData基础常量
 * @date 2022/11/26
 */
public class FastDataConstants {

    /**
     * 语言
     */
    public enum Language {
        ZH_CN,
        EN_US,
    }

    /**
     * 请求日期类型
     */
    @Getter
    public enum QueryDateType {

        // 日榜
        DAILY(1),
        // 周榜
        WEEKLY(2),
        // 月榜
        MONTHLY(3),
        ;

        private final int value;

        QueryDateType(int value) {
            this.value = value;
        }
    }

    /**
     * 达人榜单类型
     */
    @Getter
    public enum QueryAuthorType {

        // 涨粉榜
        FOLLOWER(1),
        // 蓝V榜
        VERIIED(2),
        // 热门榜
        TOP(3),
        ;

        private final int value;

        QueryAuthorType(int value) {
            this.value = value;
        }
    }

    /**
     * Response状态码
     */
    @Getter
    public enum ResponseCode {

        SUCCESS("200"),
        // 权限不足
        MAG_AUTH_3004("MAG_AUTH_3004"),
        ;

        private final String value;

        ResponseCode(String value) {
            this.value = value;
        }
    }

}
