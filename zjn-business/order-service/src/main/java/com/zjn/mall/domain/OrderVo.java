package com.zjn.mall.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 张健宁
 * @ClassName OrderVo
 * @Description 小程序：订单展示页面
 * @createTime 2024年09月19日 23:55:00
 */
@ApiModel("小程序：订单展示页面对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderVo {

    @ApiModelProperty("订单店铺对象集合")
    private List<ShopOrder> shopCartOrders;

    @ApiModelProperty("默认收货地址")
    private MemberAddr memberAddr;

    @ApiModelProperty("商品总数量")
    private Integer totalCount;

    @ApiModelProperty("合计 | 订单总额")
    private BigDecimal total = BigDecimal.ZERO;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("运费")
    private BigDecimal transfee = BigDecimal.ZERO;

    @ApiModelProperty("优惠金额")
    private BigDecimal shopReduce = BigDecimal.ZERO;

    @ApiModelProperty("小计")
    private BigDecimal actualTotal = BigDecimal.ZERO;



}
