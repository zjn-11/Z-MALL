package com.zjn.mall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 张健宁
 * @ClassName CartTotalAmount
 * @Description 购物车所选商品总金额对象
 * @createTime 2024年09月19日 15:55:00
 */
@ApiModel("购物车所选商品总金额对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartTotalAmount {
    @ApiModelProperty("总额")
    private BigDecimal totalMoney = BigDecimal.ZERO;
    @ApiModelProperty("合集")
    private BigDecimal finalMoney = BigDecimal.ZERO;
    @ApiModelProperty("优惠金额")
    private BigDecimal subtractMoney = BigDecimal.ZERO;
    @ApiModelProperty("运费")
    private BigDecimal transMoney = BigDecimal.ZERO;
}
