package com.zjn.mall.controller;

/**
 * @author 张健宁
 * @ClassName CategoryController
 * @Description 商品类目控制层
 * @createTime 2024年09月09日 23:14:00
 */

import com.zjn.mall.domain.Category;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("商品类目控制层")
@RestController
@RequestMapping("prod/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("查询所有商品类目")
    @GetMapping("table")
    @PreAuthorize("hasAnyAuthority('prod:category:page')")
    public Result<List<Category>> loadAllCategoryList() {
        List<Category> categoryList = categoryService.quaryAllCategoryList();
        return Result.success(categoryList);
    }


}
