package com.zjn.mall.controller;

import com.zjn.mall.domain.Member;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张健宁
 * @ClassName MemberController
 * @Description 微信小程序会员信息控制层
 * @createTime 2024年09月13日 15:54:00
 */

@Api("微信小程序会员信息控制层")
@RestController
@RequestMapping("p/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ApiOperation("小程序授权后更新会员信息")
    @PutMapping("setUserInfo")
    public Result<String> modifyMemberInfo(@RequestBody Member member) {
        Boolean modify = memberService.modifyMemberInfoByOpenId(member);
        return Result.handle(modify);
    }

    @ApiOperation("检查当前用户是否绑定了手机号")
    @GetMapping("isBindPhone")
    public Result<Boolean> loadMemberIsBindPhone() {
        Boolean flag = memberService.queryMemberIsBindPhone();
        return Result.success(flag);
    }


}
