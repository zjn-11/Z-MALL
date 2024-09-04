package com.zjn.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author 张健宁
 * @ClassName RedisCacheConfig
 * @Description redis缓存配置
 * @createTime 2024年09月04日 12:36:00
 */

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        cacheConfig.serializeValuesWith(RedisSerializationContext
                .SerializationPair.fromSerializer(RedisSerializer.json())) // value的序列化
                .entryTtl(Duration.ofDays(7)) // 默认时间为7天
                .disableCachingNullValues(); // redis的value禁止使用空值
        return cacheConfig;
    }
}
