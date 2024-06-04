package com.geekuniverse.cac.core.filter;

import cn.hutool.json.JSONUtil;
import com.geekuniverse.cac.common.constants.Constants;
import com.geekuniverse.cac.common.constants.RedisConstants;
import com.geekuniverse.cac.common.enums.SystemError;
import com.geekuniverse.cac.common.utils.KeyUtil;
import com.geekuniverse.cac.common.utils.TokenUtil;
import com.geekuniverse.cac.config.WhiteListConfig;
import com.geekuniverse.cac.core.model.TokenUser;
import com.geekuniverse.cac.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description token过滤器
 * @author 谢诗宏
 * @date 2022/12/10
 */
@Slf4j
@Component
@WebFilter(filterName = "AuthFilter",
        /**
         * 通配符（*）表示对所有的web资源进行拦截
        */
        urlPatterns = "/*"
)
public class AuthFilter implements Filter {

    @Resource
    private RedissonClient redisson;
    @Autowired
    private WhiteListConfig whiteListConfig;

    AntPathMatcher antPathMatcher = new AntPathMatcher();
    private boolean isExclusionUrl(String path) {
        List<String> exclusions = whiteListConfig.getUrls();
        if (exclusions.size() == 0) {
            return false;
        }
        return exclusions.stream().anyMatch(action -> antPathMatcher.match(action, path));
    }

    /**
     * 过滤器初始化
     * explain:在容器中创建当前过滤器的时候自动调用
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig){
        log.info("=================初始化过滤器!=================");
    }

    /**
     * 过滤器过滤操作
     * explain:过滤的具体操作
     * @param servletRequest
     * @param servletResponse
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("请求地址:"+request.getRequestURI());

        // 临时开放所有接口用于接口测试
        chain.doFilter(servletRequest,servletResponse);
        // 白名单中包含请求url，不过滤继续执行
        /*if (isExclusionUrl(request.getRequestURI())){
            // 交给下一个过滤器或servlet处理
            chain.doFilter(servletRequest,servletResponse);
        } else {
            try {
                boolean isFilter = false;
                servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());

                // 请求url不在白名单中,进行token校验
                Map<String, String> header = getHeaders(((HttpServletRequest) servletRequest));
                String token = header.get(Constants.JWT_TOKEN_HEADER);
                Result<?> result = new Result<>();
                if (StringUtils.isBlank(token)) {
                    // 未登录
                    result = Result.failed(SystemError.USER_1000);
                    isFilter = true;
                } else {
                    token = token.replace("Bearer ","");
                    if (!TokenUtil.verify(token)) {
                        // token错误
                        result = Result.failed(SystemError.USER_1003);
                        isFilter = true;
                    } else {
                        TokenUser tokenUser = TokenUtil.parse(token);
                        RBucket<String> redisValue = redisson.getBucket(KeyUtil.genKey(RedisConstants.JWT_TOKEN_PREFIX, String.valueOf(tokenUser.getId())));
                        if (StringUtils.isBlank(redisValue.get())) {
                            // token过期
                            result = Result.failed(SystemError.USER_1004);
                            isFilter = true;
                        }
                    }
                }

                if (isFilter) {
                    // token校验不通过
                    PrintWriter printWriter = servletResponse.getWriter();
                    printWriter.write(JSONUtil.toJsonStr(result));
                    printWriter.flush();
                    printWriter.close();
                    return;
                }

                // token校验通过
                chain.doFilter(servletRequest,servletResponse);
            } catch (Exception e) {
                log.error("请求异常", e);
                PrintWriter printWriter = servletResponse.getWriter();
                if (e instanceof cn.hutool.jwt.JWTException) {
                    printWriter.write(JSONUtil.toJsonStr(Result.failed(SystemError.USER_1003)));
                } else {
                    printWriter.write(JSONUtil.toJsonStr(Result.failed(SystemError.SYS_500)));
                }
                printWriter.flush();
                printWriter.close();
            }
        }*/
    }

    /**
     * 获取所有header
     * @param request
     * @return
     */
    private static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name	= enumeration.nextElement();
            String value = request.getHeader(name);
            headerMap.put(name, value);
        }
        return headerMap;
    }

    /**
     * 获取所有请求参数
     * @param request
     * @return
     */
    private static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameterMap = new HashMap<>();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name	= enumeration.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }
        return parameterMap;
    }

    /**
     * 过滤器销毁
     * explain:在容器中销毁当前过滤器的时候自动调用
     */
    @Override
    public void destroy() {
        log.info("=================销毁过滤器!=================");
    }
}