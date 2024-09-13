package com.zjn.mall.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 张健宁
 * @ClassName SecurityUser
 * @Description TODO
 * @createTime 2024年09月03日 19:51:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser implements UserDetails {
    // 商城后台管理系统用户的相关属性
    private Long userId;
    private String username;
    private String password;
    private Integer status;
    private Long shopId;
    private String loginType;
    private Set<String> perms = new HashSet<>();

    // 商城购物系统会员的相关属性
    private String openid;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * 获取密码
     * @return
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * 要求全局唯一，所以采取登录类型+id的方式实现
     * @return
     */
    @Override
    public String getUsername() {
        return this.loginType + this.userId;
    }

    /**
     * 账号是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    /**
     * 账号是否锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    /**
     * 凭证是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    /**
     * 用户是否可用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }

    /**
     * perms里面有一些可能是逗号分隔，需要分割开
     * @return
     */
    public Set<String> getPerms() {
        HashSet<String> finalPerms = new HashSet<>();
        perms.forEach(perm -> {
            // 如果包含逗号则需要分割开
            if (perm.contains(",")) {
                String[] splitPerms = perm.split(",");
                finalPerms.addAll(Arrays.asList(splitPerms));
            } else {
                finalPerms.add(perm);
            }
        });
        return finalPerms;
    }
}
