package com.zjn.mall.controller;

import com.zjn.mall.domain.Area;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName AreaController
 * @Description 地址管理控制层
 * @createTime 2024年09月11日 17:03:00
 */
@Api("地址管理控制层")
@RestController
@RequestMapping("admin/area")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @ApiOperation("查询全国地区信息列表")
    @GetMapping("list")
    @PreAuthorize("hasAnyAuthority('admin:area:list')")
    public Result<List<Area>> loadALLAreaList() {
        List<Area> areaList = areaService.queryALLAreaList();
        return Result.success(areaList);
    }

}
