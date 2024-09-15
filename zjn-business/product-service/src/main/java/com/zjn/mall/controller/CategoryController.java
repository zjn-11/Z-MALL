package com.zjn.mall.controller;

/**
 * @author 张健宁
 * @ClassName CategoryController
 * @Description 商品类目控制层
 * @createTime 2024年09月09日 23:14:00
 */

import com.zjn.mall.domain.Category;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        List<Category> categoryList = categoryService.queryAllCategoryList();
        return Result.success(categoryList);
    }

    @ApiOperation("查询所有的一级类目")
    @GetMapping("listCategory")
    @PreAuthorize("hasAnyAuthority('prod:category:page')")
    public Result<List<Category>> loadFirstCategoryList() {
        List<Category> categoryList = categoryService.queryFirstCategoryList();
        return Result.success(categoryList);
    }

    @ApiOperation("新增商品类目信息")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('prod:category:save')")
    public Result<String> saveCategory(@RequestBody Category category) {
        Boolean save = categoryService.saveCategory(category);
        return Result.handle(save);
    }

    @ApiOperation("根据id查询类目信息")
    @GetMapping("info/{categoryId}")
    @PreAuthorize("hasAnyAuthority('prod:category:info')")
    public Result<Category> loadCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.getById(categoryId);
        return Result.success(category);
    }

    @ApiOperation("修改商品类目信息")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('prod:category:update')")
    public Result<String> modifyCategory(@RequestBody Category category) {
        Boolean modify = categoryService.modifyCategory(category);
        return Result.handle(modify);
    }

    @ApiOperation("删除商品类目信息")
    @DeleteMapping("{categoryId}")
    @PreAuthorize("hasAnyAuthority('prod:category:delete')")
    public Result<String> removeCategoryById(@PathVariable Long categoryId) {
        Boolean remove = categoryService.removeCategoryById(categoryId);
        return Result.handle(remove);
    }

    @ApiOperation("小程序：查询商品类目集合")
    @GetMapping("category/list")
    public Result<List<Category>> loadWxCategoryList(@RequestParam Long parentId) {
        if (!parentId.equals(0L)) {
            throw new BusinessException("只能查询父级目录！");
        }
        List<Category> categoryList = categoryService.queryFirstCategoryList();
        return Result.success(categoryList);
    }

}
