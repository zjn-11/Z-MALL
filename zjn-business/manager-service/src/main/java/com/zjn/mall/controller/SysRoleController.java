package com.zjn.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.SysRole;
import com.zjn.mall.domain.SysUser;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("多条件分页查询系统角色")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('sys:role:page')")
    public Result<Page<SysRole>> loadSysRolePage(@RequestParam Long current,
                                                 @RequestParam Long size,
                                                 @RequestParam(required = false) String roleName) {
        // 创建分页对象
        Page<SysRole> sysRolePage = new Page<>(current, size);
        // 多条件分页查询系统管理员
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>()
                .like(StringUtils.hasText(roleName), SysRole::getRoleName, roleName)
                .orderByDesc(SysRole::getCreateTime);
        sysRolePage = sysRoleService.page(sysRolePage, queryWrapper);
        return Result.success(sysRolePage);
    }

    @ApiOperation("新增系统角色")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys:role:save')")
    public Result<String> saveSysRole(@RequestBody SysRole sysRole) {
        Boolean flag = sysRoleService.saveSysRole(sysRole);
        return Result.handle(flag);
    }

    @ApiOperation("根据id查询系统角色信息")
    @GetMapping("info/{roleId}")
    @PreAuthorize("hasAnyAuthority('sys:role:info')")
    public Result<SysRole> loadSysRoleInfoById(@PathVariable Long roleId) {
        SysRole sysrole = sysRoleService.loadSysRoleInfoById(roleId);
        return Result.success(sysrole);
    }

    @ApiOperation("修改系统角色")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('sys:role:update')")
    public Result<String> modifySysRoleInfo(@RequestBody SysRole sysRole) {
        Boolean modify = sysRoleService.modifySysRoleInfo(sysRole);
        return Result.handle(modify);
    }

    @ApiOperation("单个或批量删除系统角色")
    @DeleteMapping()
    @PreAuthorize("hasAnyAuthority('sys:role:delete')")
    public Result<String> removeSysRole(@RequestBody List<Long> roleIdList) {
        Boolean remove = sysRoleService.removeSysRole(roleIdList);
        return Result.handle(remove);
    }

}
