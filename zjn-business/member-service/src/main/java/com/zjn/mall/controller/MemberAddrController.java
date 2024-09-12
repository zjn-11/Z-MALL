package com.zjn.mall.controller;

import com.zjn.mall.domain.MemberAddr;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.MemberAddrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张健宁
 * @ClassName MemberAddrController
 * @Description 会员模块收货地址管理控制层
 * @createTime 2024年09月12日 17:46:00
 */
@Api("后台会员收货地址管理接口")
@RestController
@RequestMapping("p/address")
@RequiredArgsConstructor
public class MemberAddrController {

    private final MemberAddrService memberAddrService;

    @ApiOperation("根据地址id查询地址信息")
    @GetMapping("getMemberAddrById")
    public Result<MemberAddr> getMemberAddrById(@RequestParam Long addrId){
        MemberAddr memberAddr = memberAddrService.getById(addrId);
        return Result.success(memberAddr);
    }
}
