package com.zjn.mall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName ShopCart
 * @Description 购物车店铺对象
 * @createTime 2024年09月19日 11:49:00
 */
@ApiModel("购物车店铺对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCart {
    @ApiModelProperty("店铺标识")
    private Long shopId;

    @ApiModelProperty("店铺对应商品条目集合")
    private List<CartItem> shopCartItems;
}
