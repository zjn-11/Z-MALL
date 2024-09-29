package com.zjn.mall.strategy.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.domain.LoginSysUser;
import com.zjn.mall.mapper.LoginSysUserMapper;
import com.zjn.mall.model.SecurityUser;
import com.zjn.mall.strategy.LoginStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author 张健宁
 * @ClassName SysUserLoginStrategy
 * @Description 商城后台管理系统登录具体实现
 * @createTime 2024年09月03日 19:34:00
 */
@Service(AuthConstants.SYS_USER_LOGIN)
@RequiredArgsConstructor
public class SysUserLoginStrategy implements LoginStrategy {

    private final LoginSysUserMapper loginSysUserMapper;

    @Override
    public UserDetails realLogin(String username) {
        // 查询用户
        LoginSysUser loginSysUser = loginSysUserMapper.selectOne(new LambdaQueryWrapper<LoginSysUser>()
                .eq(LoginSysUser::getUsername, username)
        );
        if (ObjectUtil.isNotNull(loginSysUser)) {
            // 查询用户操作权限
            Set<String> perms = loginSysUserMapper.selectPermsByUserId(loginSysUser.getUserId());
            SecurityUser securityUser = new SecurityUser();
            securityUser.setUserId(loginSysUser.getUserId());
            securityUser.setUsername(loginSysUser.getUsername());
            securityUser.setPassword(loginSysUser.getPassword());
            securityUser.setStatus(loginSysUser.getStatus());
            securityUser.setPassword(loginSysUser.getPassword());
            securityUser.setLoginType(AuthConstants.SYS_USER_LOGIN);
            // 判断是否有权限
            if (CollUtil.isNotEmpty(perms) && perms.size() != 0)
                securityUser.setPerms(perms);
            return securityUser;
        }
        return null;
    }
}
