package com.zjn.mall.service;

import com.zjn.mall.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @ClassName SysMenuService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月05日 00:46:00
 */

public interface SysMenuService extends IService<SysMenu>{


    Set<SysMenu> queryUserMenusListByUserId(Long loginUserId);

    List<SysMenu> loadAllSysMenuList();

    boolean saveSysMenu(SysMenu sysMenu);

    Boolean modifySysMenu(SysMenu sysMenu);
}
