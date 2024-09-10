package com.zjn.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.ProdProp;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.ProdPropService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张健宁
 * @ClassName ProdSpecController
 * @Description 商品规格管理控制层
 * @createTime 2024年09月10日 17:25:00
 */

@Api("商品规格管理控制层")
@RestController
@RequestMapping("prod/spec")
@RequiredArgsConstructor
public class ProdSpecController {

    private final ProdPropService prodPropService;

    @ApiOperation("分页查询商品规格")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('prod:spec:page')")
    public Result<Page<ProdProp>> loadProdSpecPage(@RequestParam Long current,
                                                   @RequestParam Long size,
                                                   @RequestParam(required = false) String propName) {
        Page<ProdProp> prodPropPage = prodPropService.queryProdSpecPage(current, size, propName);
        return Result.success(prodPropPage);
    }

    @ApiOperation("新增商品规格")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('prod:spec:save')")
    public Result<String> saveProdSpec(@RequestBody ProdProp prodProp) {
        Boolean save = prodPropService.saveProdSpec(prodProp);
        return Result.handle(save);
    }

    @ApiOperation("修改商品规格")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('prod:spec:update')")
    public Result<String> modifyProdSpec(@RequestBody ProdProp prodProp) {
        Boolean modify = prodPropService.modifyProdSpec(prodProp);
        return Result.handle(modify);
    }

    @ApiOperation("删除商品属性规格")
    @DeleteMapping("{propId}")
    @PreAuthorize("hasAnyAuthority('prod:spec:delete')")
    public Result<String> removeProdSpecById(@PathVariable Long propId) {
        Boolean remove = prodPropService.removeProdSpecById(propId);
        return Result.handle(remove);
    }

}
