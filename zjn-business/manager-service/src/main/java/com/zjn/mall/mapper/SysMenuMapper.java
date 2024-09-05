package com.zjn.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjn.mall.domain.SysMenu;

import java.util.Set;

/**
 * @ClassName SysMenuMapper
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月05日 00:46:00
 */

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    Set<SysMenu> queryUserMenusListByUserId(Long loginUserId);
}