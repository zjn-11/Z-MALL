package com.zjn.mall.controller;

import com.zjn.mall.domain.SysMenu;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.SysMenuService;
import com.zjn.mall.util.AuthUtils;
import com.zjn.mall.vo.MenuAndAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author 张健宁
 * @ClassName SysMenuController
 * @Description 系统权限控制层
 * @createTime 2024年09月05日 14:25:00
 */

@Api(tags = "系统权限接口管理")
@RestController
@RequestMapping("/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @ApiOperation("查询用户的菜单和操作权限")
    @GetMapping("nav")
    public Result<MenuAndAuth> loadUserMenuAndAuth() {
        // 查询用户id
        Long loginUserId = AuthUtils.getLoginUserId();
        // 获取操作权限
        Set<String> perms = AuthUtils.getLoginUserPerms();
        // 获取菜单权限
        Set<SysMenu> sysMenus = sysMenuService.queryUserMenusListByUserId(loginUserId);
        // 菜单和操作权限对象封装
        MenuAndAuth menuAndAuth = new MenuAndAuth(sysMenus, perms);
        // 返回结果
        return Result.success(menuAndAuth);
    }

    @ApiOperation("查询系统所有权限集合")
    @GetMapping("table")
    @PreAuthorize("hasAnyAuthority('sys:menu:list')")
    public Result<List<SysMenu>> loadAllSysMenuList() {
        List<SysMenu> menuList = sysMenuService.loadAllSysMenuList();
        return Result.success(menuList);
    }

}
