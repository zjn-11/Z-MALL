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

    /**
     * 登录url
     */
    String LOGIN_URL = "/doLogin";

    /**
     * 登出url
     */
    String LOGOUT_URL = "/doLogout";

    /**
     * 登录类型
     *
     */
    String LOGIN_TYPE = "loginType";

    /**
     * 登录类型值：商城后台管理
     */
    String SYS_USER_LOGIN = "sysUserLogin";

    /**
     * 登录类型值：商城会员管理
     */
    String MEMBER_LOGIN = "memberLogin";

    /**
     * 过期时间
     * unit: s, 4个小时
     */
    Long TOKEN_TIME = 14400L;

}
