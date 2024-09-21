package com.zjn.mall.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author 张健宁
 * @ClassName SnowFlakeConfig
 * @Description TODO
 * @createTime 2024年09月20日 21:23:00
 */
@Configuration
public class SnowFlakeConfig {
    @Bean
    public Snowflake snowflake() {
        return new Snowflake(0L, 0L);
    }
}
