package com.zjn.mall.constants;

/**
 * @author 张健宁
 * @ClassName AuthConstants
 * @Description 认证授权常量类
 * @createTime 2024年09月03日 17:43:00
 */
public interface AuthConstants {
    /**
     * 请求头中token的key
     */
    String AUTHORIZATION = "Authorization";

    /**
     * token值的前缀
     */
    String BEARER = "bearer ";

    /**
     * token值存放在redis中的key
     */
    String LOGIN_TOKEN_PREFIX = "login_token:";

}
