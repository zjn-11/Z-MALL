package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjn.mall.domain.SysUserRole;
import com.zjn.mall.mapper.SysUserRoleMapper;
import com.zjn.mall.service.SysUserRoleService;
import com.zjn.mall.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.SysUserMapper;
import com.zjn.mall.domain.SysUser;
import com.zjn.mall.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysUserServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月05日 00:46:00
 */

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleService sysUserRoleService;

    /**
     * 新增管理员
     *  1. 新增管理员
     *  2. 新增管理员和角色的关系
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveSysUser(SysUser sysUser) {
        // 新增管理员
        sysUser.setCreateUserId(AuthUtils.getLoginUserId());
        sysUser.setCreateTime(new Date());
        sysUser.setShopId(1L);
        int countUser = sysUserMapper.insert(sysUser);
        ArrayList<SysUserRole> sysUserRoles = new ArrayList<>();
        if (countUser > 0) {
            // 新增管理员和角色关系
            List<Long> roleIdList = sysUser.getRoleIdList();
            if (CollectionUtil.isNotEmpty(roleIdList) && roleIdList.size() != 0) {
                roleIdList.forEach(roleId -> {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(sysUser.getUserId());
                    sysUserRole.setRoleId(roleId);
                    sysUserRoles.add(sysUserRole);
                });
                sysUserRoleService.saveBatch(sysUserRoles);
            }
        }
        return countUser;
    }
}
