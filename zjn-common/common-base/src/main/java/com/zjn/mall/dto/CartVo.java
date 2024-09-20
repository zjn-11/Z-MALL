package com.zjn.mall.dto;

import com.zjn.mall.dto.ShopCart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName CartVo
 * @Description 购物车对象
 * @createTime 2024年09月19日 11:51:00
 */
@ApiModel("购物车对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartVo {

    @ApiModelProperty("购物车店铺对象集合")
    private List<ShopCart> shopCarts;
}
