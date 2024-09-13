package com.zjn.mall.service;

import com.zjn.mall.domain.Member;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName MemberService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 12:40:00
 */

public interface MemberService extends IService<Member>{

    Boolean removeMemberByIds(List<Long> ids);

    Boolean modifyMemberInfoByOpenId(Member member);
}
