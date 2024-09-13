package com.zjn.mall.controller;

import com.zjn.mall.model.Result;
import com.zjn.mall.service.OrderService;
import com.zjn.mall.vo.OrderStatusCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
