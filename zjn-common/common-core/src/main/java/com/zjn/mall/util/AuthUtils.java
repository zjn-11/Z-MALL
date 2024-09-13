package com.zjn.mall.util;

import com.zjn.mall.model.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

/**
 * @author 张健宁
 * @ClassName AuthUtils
 * @Description 用户认证授权工具类
 * @createTime 2024年09月05日 14:19:00
 */
public class AuthUtils {

    /**
     * 获取Security容器中的认证用户对象
     * @return
     */
    public static SecurityUser getLoginUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取Security容器中认证用户对象的id
     * @return
     */
    public static Long getLoginUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取Security容器中认证用户对象的操作权限
     * @return
     */
    public static Set<String> getLoginUserPerms() {
        return getLoginUser().getPerms();
    }

    public static String getLoginMemberOpenid() {
        return getLoginUser().getOpenid();
    }
}
