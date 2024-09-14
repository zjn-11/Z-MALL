package com.zjn.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.MemberAddr;
import com.zjn.mall.mapper.MemberAddrMapper;
import com.zjn.mall.service.MemberAddrService;
/**
 * @ClassName MemberAddrServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 12:40:00
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "com.zjn.mall.service.impl.MemberAddrServiceImpl")
public class MemberAddrServiceImpl extends ServiceImpl<MemberAddrMapper, MemberAddr> implements MemberAddrService{

    private final MemberAddrMapper memberAddrMapper;

    @Override
    @Cacheable(key = "#openid")
    public List<MemberAddr> queryMemberAddrByOpenId(String openid) {
        List<MemberAddr> addrList = memberAddrMapper.selectList(
                new LambdaQueryWrapper<MemberAddr>()
                        .eq(MemberAddr::getOpenId, openid)
                        .eq(MemberAddr::getStatus, 1)
                        .orderByDesc(MemberAddr::getCommonAddr, MemberAddr::getCreateTime)
        );
        return addrList;
    }
}
