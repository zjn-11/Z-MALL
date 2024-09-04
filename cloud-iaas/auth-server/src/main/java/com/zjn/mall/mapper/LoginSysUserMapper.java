package com.zjn.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjn.mall.domain.LoginSysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * @ClassName LoginSysUserMapper
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 19:46:00
 */

public interface LoginSysUserMapper extends BaseMapper<LoginSysUser> {
    /**
     * 根据用户标识查询用户的权限集合
     * @param userId
     * @return
     */
    Set<String> selectPermsByUserId(Long userId);
}