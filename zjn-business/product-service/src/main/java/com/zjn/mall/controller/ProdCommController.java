package com.zjn.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.ProdComm;
import com.zjn.mall.dto.ProdCommonViewDto;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.ProdCommService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName ProdCommController
 * @Description 商品评论管理控制层
 * @createTime 2024年09月10日 22:43:00
 */

@Api("商品评论管理控制层")
@RestController
@RequestMapping("prod/prodComm")
@RequiredArgsConstructor
public class ProdCommController {

    private final ProdCommService prodCommService;

    @ApiOperation("分页查询商品评论")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('prod:prodComm:page')")
    public Result<Page<ProdComm>> loadProdCommPage(@RequestParam Long current,
                                           @RequestParam Long size,
                                           @RequestParam(required = false) String prodName,
                                           @RequestParam(required = false) Integer status) {
        Page<ProdComm> prodCommPage = new Page<>(current, size);
        prodCommPage = prodCommService.page(prodCommPage,
                new LambdaQueryWrapper<ProdComm>()
                        .like(StringUtils.hasText(prodName), ProdComm::getProdName, prodName)
                        .eq(ObjectUtil.isNotNull(status), ProdComm::getStatus, status)
                        .orderByDesc(ProdComm::getCreateTime)
        );
        return Result.success(prodCommPage);
    }

    @ApiOperation("查看商品评论详情消息")
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('prod:prodComm:info')")
    public Result<ProdComm> loadProdCommInfo(@PathVariable Long id) {
        ProdComm prodComm = prodCommService.getById(id);
        return Result.success(prodComm);
    }

    @ApiOperation("编辑商品评论信息")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('prod:prodComm:update')")
    public Result<String> modifyProdComm(@RequestBody ProdComm prodComm) {
        Boolean modify = prodCommService.modifyProdComm(prodComm);
        return Result.handle(modify);
    }

    @ApiOperation("小程序：根据商品Id返回商品评论总览数据")
    @GetMapping("prodComm/prodCommData")
    public Result<ProdCommonViewDto> loadProdCommonViewByProdId(@RequestParam Long prodId) {
        ProdCommonViewDto prodCommonViewDto = prodCommService.ProdCommonViewByProdId(prodId);
        return Result.success(prodCommonViewDto);
    }

    @ApiOperation("小程序：多条件分页查询评论信息")
    @GetMapping("prodComm/prodCommPageByProd")
    public Result<Page<ProdComm>> loadWxProdCommPageByProd(@RequestParam Long prodId,
                                                   @RequestParam Long size,
                                                   @RequestParam Long current,
                                                   @RequestParam Integer evaluate) {
        Page<ProdComm> prodCommPage = prodCommService.queryWxProdCommPageByProd(prodId, size, current, evaluate);
        return Result.success(prodCommPage);
    }
}
