package com.zjn.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.SysLog;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张健宁
 * @ClassName SysLogController
 * @Description 系统操作日志管理控制层
 * @createTime 2024年09月08日 18:04:00
 */

@Api(value = "系统操作日志管理控制层")
@RestController
@RequestMapping("sys/log")
@RequiredArgsConstructor
public class SysLogController {

    public final SysLogService sysLogService;

    @ApiOperation("多条件分页查询系统操作日志")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('sys:log:page')")
    public Result<Page<SysLog>> loadSysLogPage(
            @RequestParam Long current,
            @RequestParam Long size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String operation
    ) {
        Page<SysLog> sysLogPage = new Page<>(current, size);
        sysLogPage = sysLogService.page(sysLogPage,
                new LambdaQueryWrapper<SysLog>()
                        .eq(ObjectUtil.isNotEmpty(userId), SysLog::getUserId, userId)
                        .like(StringUtils.hasText(operation), SysLog::getOperation, operation)
        );
        return Result.success(sysLogPage);
    }

}
