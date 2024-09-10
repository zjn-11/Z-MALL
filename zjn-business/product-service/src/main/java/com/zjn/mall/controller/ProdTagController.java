package com.zjn.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.ProdTag;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.ProdTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName TagController
 * @Description 分组标签控制层
 * @createTime 2024年09月10日 16:26:00
 */

@Api("分组标签控制层")
@RestController
@RequestMapping("prod/prodTag")
@RequiredArgsConstructor
public class ProdTagController {

    private final ProdTagService prodTagService;

    @ApiOperation("分页查询商品分组标签")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('prod:prodTag:page')")
    public Result<Page<ProdTag>> loadProdTagPage(@RequestParam Long current,
                                                 @RequestParam Long size,
                                                 @RequestParam(required = false) String title,
                                                 @RequestParam(required = false) Integer status) {
        Page<ProdTag> prodTagPage = new Page<>(current, size);
        prodTagPage = prodTagService.page(prodTagPage,
                new LambdaQueryWrapper<ProdTag>()
                        .like(StringUtils.hasText(title), ProdTag::getTitle, title)
                        .eq(ObjectUtil.isNotNull(status), ProdTag::getStatus, status)
                        .orderByDesc(ProdTag::getSeq)
        );
        return Result.success(prodTagPage);
    }

    @ApiOperation("新增商品分组标签")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('prod:prodTag:save')")
    public Result<String> saveProdTag(@RequestBody ProdTag prodTag) {
        Boolean save = prodTagService.saveProdTag(prodTag);
        return Result.handle(save);
    }

    @ApiOperation("根据id查询商品分组标签")
    @GetMapping("info/{id}")
    @PreAuthorize("hasAnyAuthority('prod:prodTag:info')")
    public Result<ProdTag> loadProdTagById(@PathVariable Long id) {
        ProdTag prodTag = prodTagService.getById(id);
        return Result.success(prodTag);
    }

    @ApiOperation("修改商品分组标签")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('prod:prodTag:update')")
    public Result<String> modifyProdTag(@RequestBody ProdTag prodTag) {
        Boolean modify = prodTagService.modifyProdTag(prodTag);
        return Result.handle(modify);
    }

    @ApiOperation("删除商品分组标签")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('prod:prodTag:delete')")
    public Result<String> removeProdTagById(@PathVariable Long id) {
        Boolean remove = prodTagService.removeProdTagById(id);
        return Result.handle(remove);
    }

    @ApiOperation("查询分组标签集合")
    @GetMapping("listTagList")
    @PreAuthorize("hasAnyAuthority('prod:prodTag:page')")
    public Result<List<ProdTag>> loadProdTagList() {
        List<ProdTag> prodTagList = prodTagService.loadProdTagList();
        return Result.success(prodTagList);
    }
}
