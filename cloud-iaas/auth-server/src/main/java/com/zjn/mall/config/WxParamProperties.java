package com.zjn.mall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author 张健宁
 * @ClassName WxParamProperties
 * @Description 微信小程序参数配置类
 * @createTime 2024年09月13日 11:00:00
 */

@Component
@Data
@ConfigurationProperties(prefix = "wx")
@RefreshScope
public class WxParamProperties {

    /**
     * 小程序appId
     */
    private String appid;

    /**
     * 小程序appSecret
     */
    private String secret;

    /**
     * 固定的密码：WECHAT
     */
    private String pwd;

    /**
     * 小程序登录凭证校验接口的url地址
     */
    private String url;

}
