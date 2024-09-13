package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.service.MemberAddrService;
import com.zjn.mall.service.MemberCollectionService;
import com.zjn.mall.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.Member;
import com.zjn.mall.mapper.MemberMapper;
import com.zjn.mall.service.MemberService;

/**
 * @author 张健宁
 * @ClassName MemberServiceImpl
 * @Description TODO
 * @createTime 2024年09月12日 12:40:00
 */

@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    private final MemberMapper memberMapper;
    private final MemberAddrService memberAddrService;
    private final MemberCollectionService memberCollectionService;

    /**
     * 删除用户信息：
     * 还需要地址和收藏信息
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean removeMemberByIds(List<Long> ids) {
        List<Member> members = listByIds(ids);
        if (CollUtil.isNotEmpty(members)) {
//            List<String> openIds = members.stream().map(Member::getOpenId).collect(Collectors.toList());
//            memberAddrService.removeByIds(openIds);
//            memberCollectionService.removeByIds(openIds);
            members.forEach(member -> {
                member.setStatus(-1);
            });
        }
        return updateBatchById(members);
    }

    @Override
    public Boolean modifyMemberInfoByOpenId(Member member) {
        // 获取会员openid
        String openid = AuthUtils.getLoginMemberOpenid();
        member.setOpenId(openid);
        int update = memberMapper.update(member,
                new LambdaQueryWrapper<Member>()
                        .eq(Member::getOpenId, openid)
        );
        return update > 0;
    }
}
