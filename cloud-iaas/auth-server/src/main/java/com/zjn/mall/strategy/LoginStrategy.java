package com.zjn.mall.strategy;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author 张健宁
 * @ClassName LoginStrategy
 * @Description 策略模式：登录接口
 * @createTime 2024年09月03日 19:33:00
 */
public interface LoginStrategy {
    UserDetails realLogin(String username);
}
