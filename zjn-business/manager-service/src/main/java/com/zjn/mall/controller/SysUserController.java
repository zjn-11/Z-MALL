package com.zjn.mall.controller;

import com.zjn.mall.domain.SysUser;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.SysUserService;
import com.zjn.mall.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
