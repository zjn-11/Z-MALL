package com.zjn.mall.controller;

import com.zjn.mall.domain.SysRole;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName SysRoleController
 * @Description 系统角色管理控制层
 * @createTime 2024年09月06日 23:20:00
 */

@Api("系统角色管理控制层")
@RestController
@RequestMapping("sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @ApiOperation("查询系统所有角色")
    @GetMapping("list")
    @PreAuthorize("hasAnyAuthority('sys:role:list')")
    public Result<List<SysRole>> loadSysRoleList() {
        List<SysRole> roleList = sysRoleService.querySysRoleList();
        return Result.success(roleList);
    }

}
