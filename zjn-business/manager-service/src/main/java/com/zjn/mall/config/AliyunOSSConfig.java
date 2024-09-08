package com.zjn.mall.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 张健宁
 * @ClassName AliyunOSSConfig
 * @Description OSS的配置属性类
 * @createTime 2024年09月08日 22:50:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aliyun.oss")
@Component
public class AliyunOSSConfig {

    /**
     * OSS的外网访问地址
     */
    private String endpoint;

    /**
     * bucket名称
     */
    private String bucketName;

    /**
     * OSS的accessId
     */
    private String ACCESS_KEY_ID;

    /**
     * OSS的accessSecret
     */
    private String ACCESS_KEY_SECRET;
}
