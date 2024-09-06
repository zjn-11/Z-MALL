package com.zjn.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.SysMenu;
import com.zjn.mall.mapper.SysMenuMapper;
import com.zjn.mall.service.SysMenuService;
/**
 * @ClassName SysMenuServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月05日 00:46:00
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "com.zjn.mall.service.impl.SysMenuServiceImpl") // 还需要在方法上配置Cacheable
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{

    private final SysMenuMapper sysMenuMapper;

    /**
     * 根据用户id查询用户菜单权限
     * @param loginUserId
     * @return
     */
    @Override
    @Cacheable(key = "#loginUserId") // 缓存用户菜单权限到redis中
    public Set<SysMenu> queryUserMenusListByUserId(Long loginUserId) {
        Set<SysMenu> sysMenus = sysMenuMapper.queryUserMenusListByUserId(loginUserId);
        // 根据菜单权限的所有根目录(parent_id = 0)，给出菜单权限的层级关系的树结构
        return transformTree(sysMenus, 0L);
    }

    private Set<SysMenu> transformTree(Set<SysMenu> menus, Long parentId) {
        // 从菜单集合中获取根节点集合
//        Set<SysMenu> roots = menus.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toSet());
//        roots.forEach(root -> {
//            // 找到menus中parentId == root.id的所有菜单
//            Set<SysMenu> child = menus.stream().filter(menu -> menu.getParentId().equals(root.getMenuId())).collect(Collectors.toSet());
//            // 存入每个根目录对应的菜单对象
//            root.setChild(child);
//        });
        // 从菜单集合中获取根节点集合
        Set<SysMenu> roots = menus.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toSet());
        // 循环节点集合
        roots.forEach(root -> root.setList(transformTree(menus, root.getMenuId())));
        return roots;
    }
}
