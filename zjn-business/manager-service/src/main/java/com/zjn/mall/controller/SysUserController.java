package com.zjn.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.SysUser;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.SysUserService;
import com.zjn.mall.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张健宁
 * @ClassName SysUserController
 * @Description 系统管理员控制层
 * @createTime 2024年09月06日 17:50:00
 */

@Api(tags = "系统管理员控制层")
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @ApiOperation("查询登录用户信息")
    @GetMapping("info")
    public Result<SysUser> loadSysUserInfo() {
        // 获取登录用户信息
        Long loginUserId = AuthUtils.getLoginUserId();
        SysUser sysUser = sysUserService.getById(loginUserId);
        return Result.success(sysUser);
    }

    @ApiOperation("多条件分页查询系统管理员")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('sys:user:page')")
    public Result<Page<SysUser>> loadSysUserPage(@RequestParam Long current,
                                                 @RequestParam Long size,
                                                 @RequestParam(required = false) String username) {
        // 创建分页对象
        Page<SysUser> sysUserPage = new Page<>(current, size);
        // 多条件分页查询系统管理员
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.hasText(username), SysUser::getUsername, username)
                .orderByDesc(SysUser::getCreateTime);
        sysUserPage = sysUserService.page(sysUserPage, queryWrapper);
        return Result.success(sysUserPage);
    }

    @ApiOperation("新增管理员")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys:user:save')")
    public Result<String> saveSysUser(@RequestBody SysUser sysUser) {
        Integer count = sysUserService.saveSysUser(sysUser);
        return Result.handle(count > 0);
    }
}
