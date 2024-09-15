package com.zjn.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张健宁
 * @ClassName SearchController
 * @Description 搜索业务控制层
 * @createTime 2024年09月15日 22:02:00
 */

@Api("搜索业务控制层")
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @ApiOperation("小程序：根据商品分组标签id分页查询产品")
    @GetMapping("prod/prodListByTagId")
    public Result<Page<Prod>> loadWxProdPageByTagId(@RequestParam(defaultValue = "1") Long current,
                                                    @RequestParam Long size,
                                                    @RequestParam Long tagId) {
        Page<Prod> prodPage = searchService.queryWxProdPageByTagId(current, size, tagId);
        return Result.success(prodPage);
    }
}
