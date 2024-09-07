package com.zjn.mall.service;

import com.zjn.mall.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName SysUserService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月05日 00:46:00
 */

public interface SysUserService extends IService<SysUser>{

    /**
     * 新增管理员
     *
     * @param sysUser
     * @return
     */
    Integer saveSysUser(SysUser sysUser);

    /**
     * 通过id查询管理员信息
     * 并存入：查询出对应的角色id集合
     * @param id
     * @return
     */
    SysUser querySysUserInfoById(Long id);

    /**
     * 修改管理员信息
     * @return
     */
    Integer modifySysUserInfo(SysUser sysUser);

    /**
     * 根据id集合删除系统用户信息
     * @param userIds
     * @return
     */
    Boolean removeSysUserListByUserIds(List<Long> userIds);
}
