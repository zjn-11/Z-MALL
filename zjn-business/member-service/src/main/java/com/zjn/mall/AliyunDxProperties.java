package com.zjn.mall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 张健宁
 * @ClassName AliyunDxProperties
 * @Description 阿里云短信服务属性配置类
 * @createTime 2024年09月13日 17:35:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aliyun.dx")
@Component
public class AliyunDxProperties {

    /**
     * 阿里云通用id
     */
    private String accessKeyId;

    /**
     * 阿里云通用密钥
     */
    private String accessKeySecret;

    /**
     * 阿里云短信服务签名名称
     */
    private String signName;

    /**
     * 阿里云短信服务模版code
     */
    private String templateCode;

    /**
     * api访问端点
     */
    private String endpoint;

}
