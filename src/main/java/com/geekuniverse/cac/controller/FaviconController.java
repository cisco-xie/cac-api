package com.geekuniverse.cac.controller;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 定义favicon请求 让favicon消失,解决访问文档时404异常,虽然不影响业务,但是一直提示异常不舒服
 * </p>
 *
 * @author 谢诗宏
 * @since 2022-12-07
 */
@ApiIgnore
@RestController
@RequestMapping("/favicon.ico")
public class FaviconController {

    @GetMapping("")
    void returnNoFavicon() {
    }

}
