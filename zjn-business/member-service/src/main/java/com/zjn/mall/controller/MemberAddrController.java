package com.zjn.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.domain.MemberAddr;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.MemberAddrService;
import com.zjn.mall.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

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

    @ApiOperation("根据openid查询会员的收货地址")
    @GetMapping("list")
    public Result<List<MemberAddr>> loadMemberAddrByOpenId() {
        String openid = AuthUtils.getLoginMemberOpenid();
        List<MemberAddr> addrList = memberAddrService.queryMemberAddrByOpenId(openid);
        return Result.success(addrList);
    }

    @ApiOperation("新增用户收货地址")
    @PostMapping
    public Result<String> saveMemberAddr(@RequestBody MemberAddr memberAddr) {
        String openid = AuthUtils.getLoginMemberOpenid();
        Boolean save = memberAddrService.saveMemberAddr(memberAddr, openid);
        return Result.handle(save);
    }

    @ApiOperation("根据地址id查询出地址信息")
    @GetMapping("addrInfo/{addrId}")
    public Result<MemberAddr> loadMemberAddrByAddrId(@PathVariable Long addrId) {
        MemberAddr memberAddr = memberAddrService.getById(addrId);
        return Result.success(memberAddr);
    }

    @ApiOperation("修改会员收货地址信息")
    @PutMapping
    public Result<String> modifyMemberAddr(@RequestBody MemberAddr memberAddr) {
        String openid = AuthUtils.getLoginMemberOpenid();
        Boolean modify = memberAddrService.modifyMemberAddr(memberAddr, openid);
        return Result.handle(modify);
    }

    @ApiOperation("删除会员收货地址信息")
    @DeleteMapping("deleteAddr/{addrId}")
    public Result<String> removeModifyMemberAddr(@PathVariable Long addrId) {
        String openid = AuthUtils.getLoginMemberOpenid();
        Boolean remove = memberAddrService.removeModifyMemberAddr(addrId, openid);
        return Result.handle(remove);
    }

    @ApiOperation("设置默认收货地址")
    @PutMapping("defaultAddr/{addrId}")
    public Result<String> setDefaultAddr(@PathVariable Long addrId) {
        String openid = AuthUtils.getLoginMemberOpenid();
        Boolean set = memberAddrService.setDefaultAddr(addrId, openid);
        return Result.handle(set);
    }
}
