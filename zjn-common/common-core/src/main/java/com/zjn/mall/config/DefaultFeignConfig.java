package com.zjn.mall.config;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张健宁
 * @ClassName DefaultFeignConfig
 * @Description feign日志设置
 * @createTime 2024年09月12日 00:39:00
 */
@Slf4j
@Configuration
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.FULL;
    }
}
