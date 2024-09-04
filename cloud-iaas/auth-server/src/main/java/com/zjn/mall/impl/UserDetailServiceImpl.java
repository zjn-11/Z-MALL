package com.zjn.mall.impl;

import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.factory.LoginStrategyFactory;
import com.zjn.mall.strategy.LoginStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 张健宁
 * @ClassName UserDetailServiceImpl
 * @Description 项目自定义认证流程
 * @createTime 2024年09月03日 19:03:00
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final LoginStrategyFactory loginStrategyFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取登录类型
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String loginType = request.getHeader(AuthConstants.LOGIN_TYPE);
        // 判断请求的源系统
        if (!StringUtils.hasText(loginType)) {
            throw new InternalAuthenticationServiceException("非法登录类型");
        }
        // 策略模式，loginType是系统类型
        LoginStrategy instance = loginStrategyFactory.getInstance(loginType);
        return instance.realLogin(username);
    }
}
