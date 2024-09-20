package com.zjn.mall.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName ShopOrder
 * @Description 订单店铺对象
 * @createTime 2024年09月20日 00:02:00
 */
@ApiModel("订单店铺对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopOrder {
    @ApiModelProperty("店铺标识")
    private Long shopId;

    @ApiModelProperty("订单商品条目对象集合")
    private List<OrderItem> shopOrderItems;
}
