package com.zjn.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjn.mall.domain.OrderConfirmVo;
import com.zjn.mall.domain.OrderItem;
import com.zjn.mall.domain.OrderVo;
import com.zjn.mall.vo.OrderStatusCountVO;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 16:32:00
 */

public interface OrderService extends IService<Order>{


    Page<Order> queryOrderPage(Page<Order> orderPage, String orderNumber, Integer status, Date startTime, Date endTime);

    Order queryOrderDetailById(String orderNumber);

    OrderStatusCountVO queryOrderCountByStatus();

    Page<Order> queryMemberOrderPage(Page<Order> orderPage, Integer status);

    Order queryOrderDetailByOrderNumber(String orderNumber);

    Boolean receiptMemberOrder(String orderNumber);

    OrderVo queryWxOrderVo(OrderConfirmVo orderConfirmVo);

    String submitOrder(OrderVo orderVo);
}
