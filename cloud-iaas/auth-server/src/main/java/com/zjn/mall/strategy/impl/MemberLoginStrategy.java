package com.zjn.mall.strategy.impl;

import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.strategy.LoginStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author 张健宁
 * @ClassName MemberLoginStrategy
 * @Description 商城购物系统登录具体实现
 * @createTime 2024年09月03日 19:35:00
 */
@Service(AuthConstants.MEMBER_LOGIN)
public class MemberLoginStrategy implements LoginStrategy {
    @Override
    public UserDetails realLogin(String username) {
        return null;
    }
}
