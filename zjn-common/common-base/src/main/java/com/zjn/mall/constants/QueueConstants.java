package com.zjn.mall.constants;

/**
 * @author 张健宁
 * @ClassName QueueConstants
 * @Description 消息队列常量
 * @createTime 2024年09月21日 19:36:00
 */
public interface QueueConstants {

    /**
     * 延时订单队列
     */
    String DELAY_ORDER_QUEUE = "order.delay.queue";

    /**
     * 延时交换机
     */
    String DELAY_EXCHANGE_NAME = "order.delay.direct";

    /**
     * 延时订单：routing_key
     */
    String DELAY_ORDER_KEY = "order.delay.query";
}
