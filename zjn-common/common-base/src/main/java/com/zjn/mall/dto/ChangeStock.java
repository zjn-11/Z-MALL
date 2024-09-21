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
 * @ClassName ChangeStock
 * @Description 订单库存变更对象
 * @createTime 2024年09月20日 16:31:00
 */
@ApiModel("订单库存变更对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeStock {
    @ApiModelProperty("sku变更对象集合")
    private List<SkuChange> skuChangeList;

    @ApiModelProperty("prod变更对象集合")
    private List<ProdChange> prodChangeList;
}
