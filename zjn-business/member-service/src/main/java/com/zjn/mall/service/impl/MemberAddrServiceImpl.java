package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.MemberAddr;
import com.zjn.mall.mapper.MemberAddrMapper;
import com.zjn.mall.service.MemberAddrService;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 查询会员收货地址，并存放到redis中
     * @param openid
     * @return
     */
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

    /**
     * 新增会员收货地址
     * 如果是会员的第一个地址，则设置为默认地址
     * @param memberAddr
     * @return
     */
    @Override
    @CacheEvict(key = "#openid")
    public Boolean saveMemberAddr(MemberAddr memberAddr, String openid) {
        Integer count = memberAddrMapper.selectCount(
                new LambdaQueryWrapper<MemberAddr>()
                        .eq(MemberAddr::getOpenId, openid)
        );
        if (count.equals(0)) {
            memberAddr.setCommonAddr(1);
        }
        memberAddr.setCreateTime(new Date());
        memberAddr.setOpenId(openid);
        memberAddr.setStatus(1);
        memberAddr.setUpdateTime(new Date());
        return memberAddrMapper.insert(memberAddr) > 0;
    }

    /**
     * 修改会员收货地址信息
     * @param memberAddr
     * @param openid
     * @return
     */
    @Override
    @CacheEvict(key = "#openid")
    public Boolean modifyMemberAddr(MemberAddr memberAddr, String openid) {
        memberAddr.setUpdateTime(new Date());
        return memberAddrMapper.update(memberAddr,
                new LambdaQueryWrapper<MemberAddr>()
                        .eq(MemberAddr::getOpenId, openid)
                ) > 0;
    }

    /**
     * 删除收货地址
     * 如果删除的是默认收货地址，则需要指定一个
     * @param addrId
     * @param openid
     * @return
     */
    @Override
    @CacheEvict(key = "#openid")
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeModifyMemberAddr(Long addrId, String openid) {
        MemberAddr memberAddr = memberAddrMapper.selectById(addrId);
        // 需要判断是不是默认收货地址
        if (memberAddr.getCommonAddr().equals(1)) {
            // 选取非默认收货地址中最新的一个作为新的默认地址
            List<MemberAddr> addrList = memberAddrMapper.selectList(
                    new LambdaQueryWrapper<MemberAddr>()
                            .eq(MemberAddr::getOpenId, openid)
                            .ne(MemberAddr::getCommonAddr, 1)
                            .orderByDesc(MemberAddr::getCreateTime)
            );
            if (CollUtil.isNotEmpty(addrList)) {
                MemberAddr addr = addrList.get(0);
                addr.setCommonAddr(1);
                addr.setUpdateTime(new Date());
                memberAddrMapper.updateById(addr);
            }
        }
        return memberAddrMapper.deleteById(addrId) > 0;
    }
}
