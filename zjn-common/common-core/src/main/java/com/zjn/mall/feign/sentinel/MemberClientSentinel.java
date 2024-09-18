package com.zjn.mall.feign.sentinel;

import com.zjn.mall.domain.Member;
import com.zjn.mall.domain.MemberAddr;
import com.zjn.mall.feign.MemberClient;
import com.zjn.mall.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName MemberClientSentinel
 * @Description 后台会员模块远程接口的熔断机制
 * @createTime 2024年09月12日 17:41:00
 */

@Component
@Slf4j
public class MemberClientSentinel implements MemberClient {

    @Override
    public Result<MemberAddr> getMemberAddrById(Long addrId) {
        log.error("根据addrId查询收货地址信息集合，Feign接口调用失败！！！");
        return null;
    }

    @Override
    public Result<String> getNickNameByOpenId(String openId) {
        log.error("根据openId查询会员昵称，Feign接口调用失败！！！");
        return null;
    }

    @Override
    public Result<List<Member>> getMembersByOpenidList(List<String> openidList) {
        log.error("根据openId集合查询会员集合，Feign接口调用失败！！！");
        return null;
    }
}
