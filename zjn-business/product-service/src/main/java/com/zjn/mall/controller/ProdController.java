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

import java.util.List;

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

    @ApiOperation("根据id查询商品全部信息")
    @GetMapping("info/{id}")
    @PreAuthorize("hasAnyAuthority('prod:prod:info')")
    public Result<Prod> loadProdInfoById(@PathVariable Long id) {
        Prod prod = prodService.queryProdInfoById(id);
        return Result.success(prod);
    }

    @ApiOperation("修改商品信息")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('prod:prod:update')")
    public Result<String> modifyProdInfo(@RequestBody Prod prod) {
        Boolean modify = prodService.modifyProdInfo(prod);
        return Result.handle(modify);
    }

    @ApiOperation("删除商品信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('prod:prod:delete')")
    public Result<String> removeProdById(@PathVariable Long id) {
        Boolean remove = prodService.removeProdById(id);
        return Result.handle(remove);
    }

    @ApiOperation("小程序：根据商品id查询商品信息")
    @GetMapping("prod/prodInfo")
    public Result<Prod> loadWxProdDetail(@RequestParam Long prodId) {
        Prod prod = prodService.queryProdInfoById(prodId);
        return Result.success(prod);
    }

    ///////////////////////////////////feign 接口///////////////////////////////////

    @ApiOperation("根据id集合查询商品集合")
    @GetMapping("getProdListByIds")
    public Result<List<Prod>> loadProdListByIds(@RequestParam List<Long> prodIdList) {
        List<Prod> prods = prodService.listByIds(prodIdList);
        return Result.success(prods);
    }

    @ApiOperation("根据类目id集合查询商品信息")
    @GetMapping("getProdListByCategoryIds")
    public Result<List<Prod>> getProdListByCategoryIds(@RequestParam List<Long> categoryIds) {
        List<Prod> prodList = prodService.list(
                new LambdaQueryWrapper<Prod>()
                        .in(Prod::getCategoryId, categoryIds)
                        .eq(Prod::getStatus, 1)
                        .orderByDesc(Prod::getSoldNum)
        );
        return Result.success(prodList);
    }
}
