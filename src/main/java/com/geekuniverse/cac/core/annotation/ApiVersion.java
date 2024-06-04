package com.geekuniverse.cac.core.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * @description 自定义注解接口版本管理
 * @author 谢诗宏
 * @date 2023/1/1
 */
@Target({ElementType.METHOD, ElementType.TYPE}) // Annotation所修饰的对象范围,METHOD:用于描述方法,TYPE:用于描述类、接口(包括注解类型) 或enum声明
@Retention(RetentionPolicy.RUNTIME) // 定义了该Annotation被保留的时间长短,RUNTIME:在运行时有效（即运行时保留
@Documented // 用于描述其它类型的annotation应该被作为被标注的程序成员的公共API
@Mapping
public @interface ApiVersion {
    /**
     * 	版本号，默认1
     * @return
     */
    int value() default 1;
}
