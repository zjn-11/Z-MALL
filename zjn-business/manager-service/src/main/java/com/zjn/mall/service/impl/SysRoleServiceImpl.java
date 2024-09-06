package com.zjn.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.ManagerConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.SysRole;
import com.zjn.mall.mapper.SysRoleMapper;
import com.zjn.mall.service.SysRoleService;
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
