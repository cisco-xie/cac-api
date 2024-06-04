package com.geekuniverse.cac.common.utils;

/**
 * @description redis key
 * @author 谢诗宏
 * @date 2023/1/4
 */
public class KeyUtil {

    public static final String SPLITSTR = ":";
    public static final String LIKE = "*";

    /**
     * 组织key
     *
     * @param moduleKey
     * @param keys
     * @return
     */
    public static String genKey(String moduleKey, String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(moduleKey);
        for (String key : keys) {
            sb.append(SPLITSTR);
            sb.append(key);
        }
        return sb.toString();
    }

    /**
     * 模糊组织key
     *
     * @param moduleKey
     * @param keys
     * @return
     */
    public static String genLikeKey(String moduleKey, String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(moduleKey);
        for (String key : keys) {
            sb.append(SPLITSTR);
            sb.append(key);
        }
        sb.append(SPLITSTR);
        sb.append(LIKE);
        return sb.toString();
    }

    /**
     * 组织key
     *
     * @param moduleKey
     * @param subModule
     * @param key
     * @return
     */
    public static String genKey(String moduleKey, String subModule, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(moduleKey).append(SPLITSTR).append(subModule).append(SPLITSTR).append(key);
        return sb.toString();
    }

}
