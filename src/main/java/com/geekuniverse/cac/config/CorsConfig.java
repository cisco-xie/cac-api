package com.geekuniverse.cac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域访问
 *
 * @name: CorsConfig
 * @author: 谢诗宏
 * @date: 2023-05-25 21:58
 **/
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOriginPattern("*"); // 设置允许跨域访问的源
        corsConfig.addAllowedMethod("*"); // 设置允许的请求方法，如 GET、POST、PUT、DELETE 等
        corsConfig.addAllowedHeader("*"); // 设置允许的请求头
        corsConfig.setAllowCredentials(true); // 设置是否允许发送 Cookie
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // 设置过滤路径，这里设置为所有路径
        return new CorsFilter(source);
    }
}
