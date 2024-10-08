package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mysql.cj.protocol.x.SyncMessageSender;
import com.zjn.mall.domain.SysUserRole;
import com.zjn.mall.mapper.SysUserRoleMapper;
import com.zjn.mall.service.SysUserRoleService;
import com.zjn.mall.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.SysUserMapper;
import com.zjn.mall.domain.SysUser;
import com.zjn.mall.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private final SysUserRoleMapper sysUserRoleMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 通过id查询管理员信息
     * 查询出对应的角色id集合
     * @param id
     * @return
     */
    @Override
    public SysUser querySysUserInfoById(Long id) {
        // 管理员信息
        SysUser sysUser = sysUserMapper.selectById(id);
        // 根据管理员id查询角色
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id)
        );
        if (CollectionUtil.isNotEmpty(sysUserRoles) && sysUserRoles.size() != 0) {
//            sysUserRoles.forEach(sysUserRole -> sysUser.getRoleIdList().add(sysUserRole.getRoleId()));
            List<Long> roleIdList = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            sysUser.setRoleIdList(roleIdList);
        }
        return sysUser;
    }

    /**
     * 修改管理员信息
     *  1. 删除然后添加管理员角色记录
     *  2. 修改管理员信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer modifySysUserInfo(SysUser sysUser) {
        // 删除然后添加管理员角色记录
        sysUserRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, sysUser.getUserId())
        );
        saveSysUserRole(sysUser);
        // 修改管理员信息，密码有值则需要加密，如果没有则密码不变，还需要配置密码字段的updateStrategy，保证密码为空串时也不更新
        String newPassword = sysUser.getPassword();
        if (StringUtils.hasText(newPassword)) {
            sysUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        }
        return sysUserMapper.updateById(sysUser);
    }

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
        sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
        int countUser = sysUserMapper.insert(sysUser);
        // 新增管理员和角色关系
        if (countUser > 0) {
            saveSysUserRole(sysUser);
        }
        return countUser;
    }

    public void saveSysUserRole(SysUser sysUser) {
        ArrayList<SysUserRole> sysUserRoles = new ArrayList<>();
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
}
