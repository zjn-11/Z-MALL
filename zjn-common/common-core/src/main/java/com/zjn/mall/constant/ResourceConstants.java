package com.zjn.mall.constant;

/**
 * @author 张健宁
 * @ClassName ResourceConstants
 * @Description 不需要经过security验证的请求
 * @createTime 2024年09月04日 14:04:00
 */

public interface ResourceConstants {
    /**
     * 允许访问的资源路径
     */
    String[] RESOURCE_ALLOW_URLS = {
            "/v2/api-docs",  // swagger
            "/v3/api-docs",
            "/swagger-resources/configuration/ui",  //用来获取支持的动作
            "/swagger-resources",                   //用来获取api-docs的URI
            "/swagger-resources/configuration/security",//安全选项
            "/webjars/**",
            "/swagger-ui/**",
            "/druid/**",
            "/actuator/**"
    };
}