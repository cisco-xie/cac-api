package com.geekuniverse.cac.common.constants;

import lombok.Getter;

/**
 * @author 谢诗宏
 * @description 数据库基础类型常量
 * @date 2022/11/16
 */
public class DBTypeConstants {

    /**
     * 数据状态类型枚举
     */
    @Getter
    public enum StatusEnum {

        DISABLE(0),
        ENABLE(1),

        ;

        private final Integer value;

        StatusEnum(Integer value) {
            this.value = value;
        }
    }

    /**
     * 国家模块类型枚举
     */
    @Getter
    public enum CountryModelTypeEnum {

        GOODS(1),
        AUTHOR(2),
        SHOP(3),
        VIDEO(4),
        LIVE(5),

        ;

        private final Integer value;

        CountryModelTypeEnum(Integer value) {
            this.value = value;
        }
    }

}
