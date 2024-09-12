package com.zjn.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.ManagerConstants;
import com.zjn.mall.ex.handler.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * 查询系统所有权限集合
     * @return
     */
    @Override
    @Cacheable(key = ManagerConstants.SYS_ALL_MENU_KEY)
    public List<SysMenu> loadAllSysMenuList() {
        return sysMenuMapper.selectList(null);
    }

    /**
     * 新增权限
     * @param sysMenu
     * @return
     */
    @Override
    @CacheEvict(key = ManagerConstants.SYS_ALL_MENU_KEY)
    public boolean saveSysMenu(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu) > 0;
    }

    /**
     * 修改菜单权限
     * 可能会出现：目录<->菜单的情况
     * @param sysMenu
     * @return
     */
    @Override
    @CacheEvict(key = ManagerConstants.SYS_ALL_MENU_KEY)
    public Boolean modifySysMenu(SysMenu sysMenu) {
        int type = sysMenu.getType();
        // 菜单变为目录需要指定parentId为0
        if (0 == type) sysMenu.setParentId(0L);
        return sysMenuMapper.updateById(sysMenu) > 0;
    }

    /**
     * 删除菜单权限集合
     *  如果当前菜单还有子节点，就不允许删除
     * @param menuId
     * @return
     */
    @Override
    @CacheEvict(key = ManagerConstants.SYS_ALL_MENU_KEY)
    public Boolean removeSysMenu(Long menuId) {
        // 根据id查询子菜单集合
        List<SysMenu> subMenuList = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getParentId, menuId)
        );
        if (ObjectUtil.isNotEmpty(subMenuList) && subMenuList.size() != 0) {
            // 如果当前一级是菜单不是目录，需要判断是不是只有一个一级目录
            if (subMenuList.size() == 1 && subMenuList.get(0).getParentId().equals(menuId)) {
                return sysMenuMapper.deleteById(menuId) > 0;
            }
            // 说明当前节点包含有子节点
            throw new BusinessException("当前菜单节点包含有子节点，不可删除!");
        }
        return sysMenuMapper.deleteById(menuId) > 0;
    }

    /**
     * 将菜单权限集合转换为树结构方便前端呈现
     * @param menus
     * @param parentId
     * @return
     */
    private Set<SysMenu> transformTree(Set<SysMenu> menus, Long parentId) {
        /*有多级菜单的情况*/
        // 从菜单集合中获取根节点集合
        Set<SysMenu> roots = menus.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toSet());
        // 循环节点集合
        roots.forEach(root -> root.setList(transformTree(menus, root.getMenuId())));
        return roots;

        /* 只有两层菜单结构的情况
        // 从菜单集合中获取根节点集合
        Set<SysMenu> roots = menus.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toSet());
        roots.forEach(root -> {
            // 找到menus中parentId == root.id的所有菜单
            Set<SysMenu> child = menus.stream().filter(menu -> menu.getParentId().equals(root.getMenuId())).collect(Collectors.toSet());
            // 存入每个根目录对应的菜单对象
            root.setChild(child);
        });
        */
    }
}
