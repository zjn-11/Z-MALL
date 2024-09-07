package com.zjn.mall.service;

import com.zjn.mall.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName SysRoleService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月05日 00:46:00
 */

public interface SysRoleService extends IService<SysRole>{


    /**
     * 查询所有角色列表
     * @return
     */
    List<SysRole> querySysRoleList();

    Boolean saveSysRole(SysRole sysRole);
}
