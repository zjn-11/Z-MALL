package com.zjn.mall.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张健宁
 * @ClassName RabbitMQConfig
 * @Description TODO
 * @createTime 2024年09月21日 18:43:00
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 消息转换器，可以在页面中通过JSON格式展示消息，增加可读性
     * 同时为每一条消息设置一个全局唯一的id，提供消费幂等性支持
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        // 为每条消息设置id，便于之后做幂等性处理
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;
    }
}
