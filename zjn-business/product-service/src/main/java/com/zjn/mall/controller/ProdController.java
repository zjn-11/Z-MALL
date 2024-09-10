package com.zjn.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.domain.ProdComm;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.ProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张健宁
 * @ClassName ProdController
 * @Description 商品管理控制层
 * @createTime 2024年09月11日 00:20:00
 */
@Api("商品管理控制层")
@RestController
@RequestMapping("prod/prod")
@RequiredArgsConstructor
public class ProdController {

    private final ProdService prodService;

    @ApiOperation("多条件分页查询商品信息")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('prod:prod:page')")
    public Result<Page<Prod>> loadProdPage(@RequestParam Long current,
                               @RequestParam Long size,
                               @RequestParam(required = false) String prodName,
                               @RequestParam(required = false) Integer status) {
        Page<Prod> prodPage = new Page<>(current, size);
        prodPage = prodService.page(prodPage,
                new LambdaQueryWrapper<Prod>()
                        .like(StringUtils.hasText(prodName), Prod::getProdName, prodName)
                        .eq(ObjectUtil.isNotNull(status), Prod::getStatus, status)
                        .orderByDesc(Prod::getCreateTime)
        );
        return Result.success(prodPage);
    }

    @ApiOperation("新增商品信息")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('prod:prod:save')")
    public Result<String> saveProd(@RequestBody Prod prod) {
        Boolean save = prodService.saveProd(prod);
        return Result.handle(save);
    }
}
