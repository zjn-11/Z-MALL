package com.zjn.mall.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张健宁
 * @ClassName OrderStatusCountVO
 * @Description 订单状态数量
 * @createTime 2024年09月13日 18:16:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单状态数量")
public class OrderStatusCountVO {
    @ApiModelProperty("待支付数量")
    private int unPay;
    @ApiModelProperty("待发货数量")
    private int payed;
    @ApiModelProperty("待收获数量")
    private int consignment;
}
