package com.zjn.mall.listener;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.QueueConstants;
import com.zjn.mall.domain.Order;
import com.zjn.mall.domain.OrderDelayDto;
import com.zjn.mall.dto.ChangeStock;
import com.zjn.mall.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 张健宁
 * @ClassName OrderDelayMessageListener
 * @Description 延时订单监听
 * @createTime 2024年09月21日 19:53:00
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDelayMessageListener {

    private final OrderService orderService;

    /**
     * 监听超时订单：
     * 1 需要判断订单状态
     * 1.1 如果是已支付，则直接结束
     * 1.2 如果是已支付之外的状态，需要查询订单流水
     * 1.2.1 如果是已支付，则直接删除
     * 1.2.2 如果不是，则修改状态为取消，并设置超时，同时回滚库存
     * @param orderDelayDto
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QueueConstants.DELAY_ORDER_QUEUE),
            exchange = @Exchange(value = QueueConstants.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = QueueConstants.DELAY_ORDER_KEY
    ))
    public void listenOrderDelayMessage(OrderDelayDto orderDelayDto) {
        String orderNumber = orderDelayDto.getOrderNumber();
        ChangeStock changeStock = orderDelayDto.getChangeStock();
        Order order = orderService.getOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNumber, orderNumber)
        );
        if (ObjectUtil.isEmpty(order) || !order.getStatus().equals(1)) {
            // 如果订单不存在或者订单不是未支付状态，则不做处理
            return;
        }
        // 库存的变更量当前为负数，回滚需要将其变为正
        changeStock.getProdChangeList().forEach(
                prodChange -> prodChange.setCount(prodChange.getCount() * -1)
        );
        changeStock.getSkuChangeList().forEach(
                skuChange -> skuChange.setCount(skuChange.getCount() * -1)
        );
        // 回滚库存
        orderService.cancelOrder(orderDelayDto);
    }
}
