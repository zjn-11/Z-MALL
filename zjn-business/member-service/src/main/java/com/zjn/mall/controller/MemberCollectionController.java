package com.zjn.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.MemberCollection;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.MemberCollectionService;
import com.zjn.mall.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName MemberCollectionController
 * @Description 会员收藏商品服务控制层
 * @createTime 2024年09月14日 13:52:00
 */

@Api("会员收藏商品服务控制层")
@RestController
@RequestMapping("p/collection")
@RequiredArgsConstructor
public class MemberCollectionController {

    private final MemberCollectionService memberCollectionService;

    @ApiOperation("查询收藏商品数量")
    @GetMapping("count")
    public Result<Integer> loadMemberCollectionCount() {
        String openid = AuthUtils.getLoginMemberOpenid();
        Integer count = memberCollectionService.count(
                new LambdaQueryWrapper<MemberCollection>()
                        .eq(MemberCollection::getOpenId, openid)
        );
        return Result.success(count);
    }

    @ApiOperation("分页查询收藏商品详情")
    @GetMapping("prods")
    public Result<Page<Prod>> loadMemberCollectionPage(@RequestParam Long current,
                                                   @RequestParam Long size) {
        Page<Prod> prodPage = new Page<>(current, size);
        prodPage =  memberCollectionService.queryMemberCollectionPage(prodPage);
        return Result.success(prodPage);
    }

    @ApiOperation("小程序：根据商品id查询商品是否被收藏")
    @GetMapping("isCollection")
    public Result<Boolean> checkIsCollectionByProdId(@RequestParam Long prodId) {
        String openid = AuthUtils.getLoginMemberOpenid();
        Boolean flag = memberCollectionService.checkIsCollectionByProdId(prodId, openid);
        return Result.success(flag);
    }
}
