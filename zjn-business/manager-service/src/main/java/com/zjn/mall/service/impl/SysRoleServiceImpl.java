package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.ManagerConstants;
import com.zjn.mall.domain.SysRoleMenu;
import com.zjn.mall.mapper.SysRoleMenuMapper;
import com.zjn.mall.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private final SysRoleMenuServiceImpl sysRoleMenuService;
    private final SysRoleMenuMapper sysRoleMenuMapper;

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
        // 新增角色信息
        sysRole.setCreateTime(new Date());
        sysRole.setCreateUserId(AuthUtils.getLoginUserId());
        int insert = sysRoleMapper.insert(sysRole);
        // 新增角色权限关系
        if (insert > 0) {
            saveSysRoleMenuInfo(sysRole);
        }
        return insert > 0;
    }


    /**
     * 单个或批量删除角色
     * 1. 删除角色权限集合
     * 2. 删除角色信息
     * 3. 清楚缓存
     * @param roleIdList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = ManagerConstants.SYS_ALL_ROLE_KEY)
    public Boolean removeSysRole(List<Long> roleIdList) {
        sysRoleMenuMapper.delete(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .in(SysRoleMenu::getRoleId, roleIdList)
        );
        return sysRoleMapper.deleteBatchIds(roleIdList) == roleIdList.size();
    }

    /**
     * 修改系统角色信息
     * 先修改系统角色信息
     * 删除并添加角色权限关系
     * @param sysRole
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = ManagerConstants.SYS_ALL_ROLE_KEY)
    public Boolean modifySysRoleInfo(SysRole sysRole) {
        int update = sysRoleMapper.updateById(sysRole);
        if (update > 0) {
            // 删除系统权限关系
            sysRoleMenuMapper.delete(
                    new LambdaQueryWrapper<SysRoleMenu>()
                            .eq(SysRoleMenu::getRoleId, sysRole.getRoleId())
            );
            // 新增角色权限关系
            saveSysRoleMenuInfo(sysRole);
        }
        return update > 0;
    }

    /**
     * 新增角色权限关系
     * @param sysRole
     */
    public void saveSysRoleMenuInfo(SysRole sysRole) {
        List<Long> menuIdList = sysRole.getMenuIdList();
        if (CollectionUtil.isNotEmpty(menuIdList) && menuIdList.size() != 0) {
            Long roleId = sysRole.getRoleId();
            ArrayList<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            menuIdList.forEach(menuId -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenus.add(sysRoleMenu);
            });
            sysRoleMenuService.saveBatch(sysRoleMenus);
        }
    }

    /**
     * 根据id查询角色信息
     * 根据roleId查出所有角色权限关系的权限menuId
     * @param roleId
     * @return
     */
    @Override
    public SysRole loadSysRoleInfoById(Long roleId) {
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        if (ObjectUtil.isNotEmpty(sysRole)) {
            List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(
                    new LambdaQueryWrapper<SysRoleMenu>()
                            .eq(SysRoleMenu::getRoleId, roleId)
            );
            List<Long> list = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(sysRoleMenus)) {
                 list = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            }
            sysRole.setMenuIdList(list);
        }
        return sysRole;
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
