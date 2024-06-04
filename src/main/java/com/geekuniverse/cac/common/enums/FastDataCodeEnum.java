package com.geekuniverse.cac.common.enums;

import lombok.Getter;

/**
 * @author 谢诗宏
 * @description FastData response code枚举
 * @date 2022/11/16
 */
@Getter
public enum FastDataCodeEnum {

    SUCCESS("200"),
    // 权限不足
    MAG_AUTH_3004("MAG_AUTH_3004"),
    // 权限不足
    MAG_AUTH_3006("MAG_AUTH_3006"),
    ;

    private final String value;

    FastDataCodeEnum(String value) {
        this.value = value;
    }
}
