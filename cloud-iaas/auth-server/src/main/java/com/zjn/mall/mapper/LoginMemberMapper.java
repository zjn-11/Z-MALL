package com.zjn.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjn.mall.domain.LoginMember;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMemberMapper extends BaseMapper<LoginMember> {
}