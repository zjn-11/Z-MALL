package com.zjn.mall.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.domain.MemberAddr;
import com.zjn.mall.domain.Order;
import com.zjn.mall.domain.OrderItem;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.feign.MemberClient;
import com.zjn.mall.mapper.OrderItemMapper;
import com.zjn.mall.mapper.OrderMapper;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.OrderService;
import com.zjn.mall.util.AuthUtils;
import com.zjn.mall.vo.OrderStatusCountVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张健宁
 * @ClassName OrderServiceImpl
 * @Description TODO
 * @createTime 2024年09月12日 16:32:00
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final MemberClient memberClient;

    /**
     * 多条件分页查询订单
     *
     * @param orderPage
     * @param orderNumber
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Page<Order> queryOrderPage(Page<Order> orderPage, String orderNumber, Integer status, Date startTime, Date endTime) {
        // 多条件分页查询会员
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<Order>()
                .eq(ObjectUtil.isNotNull(status), Order::getStatus, status)
                .like(StringUtils.hasText(orderNumber), Order::getOrderNumber, orderNumber)
                .between(ObjectUtil.isAllNotEmpty(startTime, endTime), Order::getCreateTime, startTime, endTime)
                .orderByDesc(Order::getCreateTime);
        orderPage = page(orderPage, queryWrapper);
        // 后台根据订单号查询出订单详情
        String loginType = AuthConstants.SYS_USER_LOGIN;
        queryOrderItemByOrderNumber(orderPage, loginType);
        return orderPage;
    }

    /**
     * 根据订单号查询出详情信息
     *
     * @param orderNumber
     * @return
     */
    @Override
    public Order queryOrderDetailById(String orderNumber) {
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNumber, orderNumber)
        );

        if (ObjectUtil.isNotEmpty(order)) {
            // 查询订单详情信息
            List<OrderItem> orderItems = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>()
                            .eq(OrderItem::getOrderNumber, orderNumber)
            );
            order.setOrderItems(orderItems);

            // 查询地址详情信息
            Result<MemberAddr> result = memberClient.getMemberAddrById(order.getAddrOrderId());
            if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
                throw new BusinessException("Feign接口调用失败：未能获取到收货地址信息！");
            }
            order.setUserAddrOrder(result.getData());

            // 查询会员昵称
            Result<String> result1 = memberClient.getNickNameByOpenId(order.getOpenId());
            if (result1.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
                throw new BusinessException("Feign接口调用失败：未能获取到用户名称！");
            }
            order.setNickName(result1.getData());
        }
        return order;
    }

    /**
     * 获取不同状态订单数量
     * 1:待付款
     * 2:待发货
     * 3:待收货
     *
     * @return
     */
    @Override
    public OrderStatusCountVO queryOrderCountByStatus() {
        String openid = AuthUtils.getLoginMemberOpenid();
        Integer unPay = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 1)
        );
        Integer payed = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 2)
        );
        Integer consignment = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 3)
        );
        return OrderStatusCountVO.builder()
                .unPay(unPay).payed(payed).consignment(consignment)
                .build();
    }

    /**
     * 多条件分页查询会员的订单
     *
     * @param orderPage
     * @param status
     * @return
     */
    @Override
    public Page<Order> queryMemberOrderPage(Page<Order> orderPage, Integer status) {
        String openid = AuthUtils.getLoginMemberOpenid();
        // 多条件分页查询会员
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(!status.equals(0), Order::getStatus, status)
                .orderByDesc(Order::getCreateTime);

        orderPage = page(orderPage, queryWrapper);

        // 小程序获取订单详情集合
        String loginType = AuthConstants.MEMBER_LOGIN;
        queryOrderItemByOrderNumber(orderPage, loginType);
        return orderPage;
    }

    /**
     * 根据订单号查询出订单详情
     *
     * @param orderPage
     */
    public void queryOrderItemByOrderNumber(Page<Order> orderPage, String loginType) {
        // 根据订单号查询出订单详情
        List<Order> orders = orderPage.getRecords();
        if (CollectionUtil.isNotEmpty(orders)) {
            List<String> orderNumbers = orders.stream().map(Order::getOrderNumber).collect(Collectors.toList());
            List<OrderItem> orderItems = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>()
                            .in(OrderItem::getOrderNumber, orderNumbers)
            );
            if (loginType.equals(AuthConstants.SYS_USER_LOGIN)) {
                orders.forEach(order -> {
                    List<OrderItem> orderItemList = orderItems.stream()
                            .filter(orderItem -> orderItem.getOrderNumber().equals(order.getOrderNumber()))
                            .collect(Collectors.toList());
                    order.setOrderItems(orderItemList);
                });
            } else {
                orders.forEach(order -> {
                    List<OrderItem> orderItemList = orderItems.stream()
                            .filter(orderItem -> orderItem.getOrderNumber().equals(order.getOrderNumber()))
                            .collect(Collectors.toList());
                    order.setOrderItemDtos(orderItemList);
                });
            }
        }
    }
}
