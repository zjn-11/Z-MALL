package com.zjn.mall.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.Member;
import com.zjn.mall.domain.Order;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.OrderItemService;
import com.zjn.mall.service.OrderService;
import com.zjn.mall.util.EasyExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张健宁
 * @ClassName SysOrderController
 * @Description 订单管理控制层
 * @createTime 2024年09月12日 16:36:00
 */

@Api("订单管理控制层")
@RestController
@RequestMapping("order/order")
@RequiredArgsConstructor
public class SysOrderController {

    private final OrderService orderService;

    @ApiOperation("多条件分页查询订单信息")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('order:order:page')")
    public Result<Page<Order>> loadOrderPage(@RequestParam Long current,
                                        @RequestParam Long size,
                                        @RequestParam(required = false) String orderNumber,
                                        @RequestParam(required = false) Integer status,
                                        @RequestParam(required = false) Date startTime,
                                        @RequestParam(required = false) Date endTime) {

        // 创建分页对象
        Page<Order> orderPage = new Page<>(current, size);
        orderPage = orderService.queryOrderPage(orderPage, orderNumber, status, startTime, endTime);
        return Result.success(orderPage);
    }

    @ApiOperation("根据订单号查询订单详情")
    @GetMapping("orderInfo/{orderNumber}")
    @PreAuthorize("hasAnyAuthority('order:order:info')")
    public Result<Order> loadOrderDetailById(@PathVariable String orderNumber) {
        Order order = orderService.queryOrderDetailById(orderNumber);
        return Result.success(order);
    }

    @ApiOperation("导出销售记录")
    @GetMapping("soldExcel")
    @PreAuthorize("hasAnyAuthority('order:order:soldExcel')")
    public Result<String> ExportSoldOrderRecordExcel() {
        // 找到所有的订单
        List<Order> list = orderService.list(
                new LambdaQueryWrapper<Order>()
                        .orderByDesc(Order::getCreateTime)
        );
        String fileName = "./text/SoldOrderRecord";
        String sheetName = "已售订单";
        EasyExcelUtils.exportExcel(fileName, sheetName, Order.class, list);
        return Result.success(null);
    }
}
