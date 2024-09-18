package com.zjn.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.Member;
import com.zjn.mall.domain.SysUser;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 张健宁
 * @ClassName SysMemberController
 * @Description 后台管理系统维护的会员管理控制层
 * @createTime 2024年09月12日 12:42:00
 */

@Api("后台会员管理接口")
@RestController
@RequestMapping("admin/user")
@RequiredArgsConstructor
public class SysMemberController {

    private final MemberService memberService;

    @ApiOperation("多条件分页查询会员信息")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('admin:user:page')")
    public Result<Page<Member>> loadMemberPage(@RequestParam Long current,
                                                @RequestParam Long size,
                                                @RequestParam(required = false) String nickName,
                                                @RequestParam(required = false) Integer status) {
        // 创建分页对象
        Page<Member> memberPage = new Page<>(current, size);
        // 多条件分页查询会员
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<Member>()
                .eq(ObjectUtil.isNotNull(status), Member::getStatus, status)
                .like(StringUtils.hasText(nickName), Member::getNickName, nickName)
                .orderByDesc(Member::getStatus);
        memberPage = memberService.page(memberPage, queryWrapper);
        return Result.success(memberPage);
    }

    @ApiOperation("根据id获取会员信息")
    @GetMapping("info/{id}")
    @PreAuthorize("hasAnyAuthority('admin:user:info')")
    public Result<Member> loadMemberById(@PathVariable Long id) {
        Member member = memberService.getOne(
                new LambdaQueryWrapper<Member>()
                        .select(Member::getId, Member::getOpenId, Member::getPic,
                                Member::getNickName, Member::getStatus)
                        .eq(Member::getId, id)
        );
        return Result.success(member);
    }

    @ApiOperation("修改会员信息")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:user:update')")
    public Result<String> modifyMemberInfo(@RequestBody Member member) {
        member.setUpdateTime(new Date());
        boolean modify = memberService.updateById(member);
        return Result.handle(modify);
    }

    @ApiOperation("批量删除会员信息")
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:user:delete')")
    public Result<String> removeMemberByIds(@RequestBody List<Long> ids) {
        Boolean remove = memberService.removeMemberByIds(ids);
        return Result.handle(remove);
    }

    @ApiOperation("根据openId查询会员昵称")
    @GetMapping("getNickNameByOpenId")
    public Result<String> getNickNameByOpenId(@RequestParam String openId) {
        Member member = memberService.getOne(
                new LambdaQueryWrapper<Member>()
                        .select(Member::getNickName)
                        .eq(Member::getOpenId, openId)
        );
        return Result.success(member.getNickName());
    }

    @ApiOperation("feign：根据openid集合获取会员信息集合")
    @GetMapping("getMembersByOpenidList")
    public Result<List<Member>> getMembersByOpenidList(@RequestParam List<String> openidList) {
        List<Member> memberList = memberService.list(
                new LambdaQueryWrapper<Member>()
                        .in(Member::getOpenId, openidList)
        );
        return Result.success(memberList);
    }
}
