package com.zjn.mall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName AuthProperties
 * @Description TODO
 * @createTime 2024年09月03日 17:23:00
 */

@Component
@Data
@ConfigurationProperties("zjn.auth")
@RefreshScope
public class AuthProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
