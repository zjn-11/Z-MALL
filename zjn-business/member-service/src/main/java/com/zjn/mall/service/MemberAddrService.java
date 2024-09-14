package com.zjn.mall.service;

import com.zjn.mall.domain.MemberAddr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName MemberAddrService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 12:40:00
 */

public interface MemberAddrService extends IService<MemberAddr>{

    List<MemberAddr> queryMemberAddrByOpenId(String openid);
}
