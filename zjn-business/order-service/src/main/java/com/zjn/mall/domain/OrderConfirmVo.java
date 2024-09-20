package com.zjn.mall.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName OrderConfirmVo
 * @Description 订单确认页面参数对象
 * @createTime 2024年09月19日 23:56:00
 */
@ApiModel("订单确认页面参数对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderConfirmVo {
    @ApiModelProperty("订单商品条目对象（接收来自商品详情页面提交订单的参数）")
    private OrderItem orderItem;
    @ApiModelProperty("购物车id集合（接收来自购物车页面提交订单的参数）")
    private List<Long> basketIds;
}
