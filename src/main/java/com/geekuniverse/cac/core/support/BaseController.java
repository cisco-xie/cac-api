package com.geekuniverse.cac.core.support;

import com.alibaba.fastjson.JSONObject;
import com.geekuniverse.cac.common.constants.Constants;
import com.geekuniverse.cac.common.utils.TokenUtil;
import com.geekuniverse.cac.core.model.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @description 公共controller基类
 * @author 谢诗宏
 * @date 2022/12/18
 */
@Slf4j
public class BaseController {

    /**
     * 得到request对象
     */
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 得到response对象
     */
    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * id字符串转换成idList
     * @param ids ids
     * @return
     */
    public List<Long> formatIds(String ids) {
    	if (ids == null) {
    		return null;
		}
        List<Long> id = new ArrayList<>();
        String[] strings = ids.split(",");
        for (String string : strings) {
            id.add(Long.valueOf(string));
        }
        return id;
    }

    /**
     * 时间查询区间字符串转换
     * @param startTime
     * @return
     */
    public Map<String, String> formatStartTime(String startTime) {
    	Map<String, String> map = new HashMap<String, String>();
    	if (StringUtils.isNotBlank(startTime)) {
        	String[] parts = startTime.split(" - ");
        	map.put("startTime", parts[0]);
        	map.put("endTime", parts[1]);
		}
        return map;
    }

    /**
     * 获取当前用户token
     * @return
     */
    public TokenUser getUser() {
        String token = getRequest().getHeader(Constants.JWT_TOKEN_HEADER);
        return TokenUtil.parse(token);
    }
}
