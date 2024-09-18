package com.zjn.mall.feign;

import com.zjn.mall.config.DefaultFeignConfig;
import com.zjn.mall.config.FeignInterceptor;
import com.zjn.mall.domain.Member;
import com.zjn.mall.domain.MemberAddr;
import com.zjn.mall.feign.sentinel.MemberClientSentinel;
import com.zjn.mall.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName MemberClient
 * @Description 会员模块远程接口
 * @createTime 2024年09月12日 17:40:00
 */

@FeignClient(value = "member-service", configuration = {FeignInterceptor.class, DefaultFeignConfig.class},
        fallback = MemberClientSentinel.class)
public interface MemberClient {
    @GetMapping("p/address/getMemberAddrById")
    Result<MemberAddr> getMemberAddrById(@RequestParam Long addrId);

    @GetMapping("admin/user/getNickNameByOpenId")
    Result<String> getNickNameByOpenId(@RequestParam String openId);

    @GetMapping("admin/user/getMembersByOpenidList")
    Result<List<Member>> getMembersByOpenidList(@RequestParam List<String> openidList);
}
