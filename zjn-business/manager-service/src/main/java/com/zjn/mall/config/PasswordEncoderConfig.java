package com.zjn.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 张健宁
 * @ClassName PasswordEncoderConfig
 * @Description 密码加密器
 * @createTime 2024年09月07日 01:27:00
 */

@Configuration
public class PasswordEncoderConfig {

    /**
     * Spring Security中的密码加密器
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
