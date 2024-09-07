package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.constants.ManagerConstants;
import com.zjn.mall.domain.SysRoleMenu;
import com.zjn.mall.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.SysRole;
import com.zjn.mall.mapper.SysRoleMapper;
import com.zjn.mall.service.SysRoleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName SysRoleServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月05日 00:46:00
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "com.zjn.mall.impl.SysRoleServiceImpl")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuServiceImpl roleMenuService;

    /**
     * 新增系统角色
     * 1. 新增系统角色信息
     * 2. 新增角色权限关系
     * 3. 因为角色会被存放在redis中，所以每次新增都需要删除缓存
     * @param sysRole
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = ManagerConstants.SYS_ALL_ROLE_KEY)
    public Boolean saveSysRole(SysRole sysRole) {
        sysRole.setCreateTime(new Date());
        sysRole.setCreateUserId(AuthUtils.getLoginUserId());
        int insert = sysRoleMapper.insert(sysRole);
        List<SysRoleMenu> list = new ArrayList<>();
        if (insert > 0) {
            Long roleId = sysRole.getRoleId();
            List<Long> menuIdList = sysRole.getMenuIdList();
            if (CollectionUtil.isNotEmpty(menuIdList) && menuIdList.size() != 0) {
                menuIdList.forEach(menuId -> {
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(roleId);
                    sysRoleMenu.setMenuId(menuId);
                    list.add(sysRoleMenu);
                });
                roleMenuService.saveBatch(list);
            }
        }
        return insert > 0;
    }

    /**
     * 查询所有角色列表
     * @return
     */
    @Override
    @Cacheable(key = ManagerConstants.SYS_ALL_ROLE_KEY)
    public List<SysRole> querySysRoleList() {
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .orderByDesc(SysRole::getCreateTime)
        );
    }
}
