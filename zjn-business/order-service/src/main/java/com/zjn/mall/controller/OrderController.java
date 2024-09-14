package com.zjn.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.Order;
import com.zjn.mall.domain.OrderItem;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.OrderService;
import com.zjn.mall.vo.OrderStatusCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 张健宁
 * @ClassName OrderController
 * @Description 微信小程序端订单控制层
 * @createTime 2024年09月13日 18:13:00
 */
@Api("微信小程序端订单控制层")
@RestController
@RequestMapping("p/myOrder")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ApiOperation("查询会员不同状态订单数量")
    @GetMapping("orderCount")
    public Result<OrderStatusCountVO> loadOrderCountByStatus() {
        OrderStatusCountVO orderStatusCountVO = orderService.queryOrderCountByStatus();
        return Result.success(orderStatusCountVO);
    }

    @ApiOperation("多条件分页查询会员不同状态订单")
    @GetMapping("myOrder")
    public Result<Page<Order>> loadMemberOrderPage(@RequestParam Long current,
                                        @RequestParam Long size,
                                        @RequestParam Integer status) {
        Page<Order> orderPage = new Page<>(current, size);
        orderPage = orderService.queryMemberOrderPage(orderPage, status);
        return Result.success(orderPage);
    }

    @ApiOperation("根据订单号查询订单详情页信息")
    @GetMapping("orderDetail")
    public Result<Order> loadOrderDetailByOrderNumber(@RequestParam String orderNumber) {
        Order orderDetail = orderService.queryOrderDetailByOrderNumber(orderNumber);
        return Result.success(orderDetail);
    }

    @ApiOperation("会员确认收货")
    @PutMapping("receipt/{orderNumber}")
    public Result<String> receiptMemberOrder(@PathVariable String orderNumber) {
        Boolean receipt = orderService.receiptMemberOrder(orderNumber);
        return Result.handle(receipt);
    }

    @ApiOperation("会员删除已完成订单")
    @DeleteMapping("{orderNumber}")
    public Result<String> removeMemberOrder(@PathVariable String orderNumber) {
        boolean update = orderService.update(
                Order.builder()
                        .deleteStatus(1)
                        .updateTime(new Date())
                        .build(),
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNumber, orderNumber)
        );
        return Result.handle(update);
    }
}
