package com.zjn.mall.factory;

import com.zjn.mall.strategy.LoginStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 张健宁
 * @ClassName LoginStrategyFactory
 * @Description 策略模式：登录工厂
 * @createTime 2024年09月03日 19:37:00
 */
@Component
//@RequiredArgsConstructor
public class LoginStrategyFactory {

    @Autowired
    private final Map<String, LoginStrategy> loginStrategyMap = new HashMap<>();

    public LoginStrategy getInstance(String loginType) {
        return loginStrategyMap.get(loginType);
    }
}
