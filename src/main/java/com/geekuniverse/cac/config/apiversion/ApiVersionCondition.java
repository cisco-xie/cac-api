package com.geekuniverse.cac.config.apiversion;


import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description API版本匹配
 * @author 谢诗宏
 * @date 2023/1/1
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    // 路径中版本的前缀， 这里用 /v[1-9]/的形式
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("/v(\\d+).*");

    private final int apiVersion;

    ApiVersionCondition(int apiVersion){
        this.apiVersion = apiVersion;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    /**
     * 将不同的筛选条件合并,这里采用的覆盖，即后来的规则生效
     * @param other
     * @return
     */
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return new ApiVersionCondition(other.getApiVersion());
    }

    /**
     * 根据request查找匹配到的筛选条件
     * @param request
     * @return
     */
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if (m.find()) {
            int version = Integer.parseInt(m.group(1));
            // 如果请求的版本号大于配置版本号， 则满足，即与请求的
            if (version >= this.apiVersion) {
                return this;
            }
        }
        return null;
    }

    /**
     * 实现不同条件类的比较，从而实现优先级排序
     * @param other
     * @param request
     * @return
     */
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        return other.getApiVersion() - this.apiVersion;
    }

}