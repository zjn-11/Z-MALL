package com.zjn.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.IndexImg;
import com.zjn.mall.domain.Notice;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.IndexImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName IndexImgController
 * @Description 轮播图管理控制层
 * @createTime 2024年09月11日 17:54:00
 */
@Api("轮播图管理控制层")
@RestController
@RequestMapping("admin/indexImg")
@RequiredArgsConstructor
public class IndexImgController {

    private final IndexImgService indexImgService;

    @ApiOperation("多条件分页查询轮播图信息")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('admin:indexImg:page')")
    public Result<Page<IndexImg>> loadIndexImgPage(@RequestParam Long current,
                                                   @RequestParam Long size,
                                                   @RequestParam(required = false) Integer status) {
        Page<IndexImg> indexImgPage = new Page<>(current, size);
        indexImgPage = indexImgService.page(indexImgPage,
                new LambdaQueryWrapper<IndexImg>()
                        .eq(ObjectUtil.isNotNull(status), IndexImg::getStatus, status)
                        .orderByDesc(IndexImg::getCreateTime)
        );

        return Result.success(indexImgPage);
    }

    @ApiOperation("新增轮播图信息")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:indexImg:save')")
    public Result<String> saveIndexImg(@RequestBody IndexImg indexImg) {
        Boolean save = indexImgService.saveIndexImg(indexImg);
        return Result.handle(save);
    }

    @ApiOperation("根据id获取轮播图信息")
    @GetMapping("info/{imgId}")
    @PreAuthorize("hasAnyAuthority('admin:indexImg:info')")
    public Result<IndexImg> loadIndexImgById(@PathVariable Long imgId) {
        IndexImg indexImg = indexImgService.queryIndexImgById(imgId);
        return Result.success(indexImg);
    }

    @ApiOperation("修改轮播图信息")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin:indexImg:update')")
    public Result<String> modifyIndexImg(@RequestBody IndexImg indexImg) {
        Boolean modify = indexImgService.modifyIndexImg(indexImg);
        return Result.handle(modify);
    }

    @ApiOperation("删除轮播图信息")
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:indexImg:delete')")
    public Result<String> removeIndexImgById(@RequestBody List<Long> imgIds) {
        boolean remove = indexImgService.removeByIds(imgIds);
        return Result.handle(remove);
    }
}
