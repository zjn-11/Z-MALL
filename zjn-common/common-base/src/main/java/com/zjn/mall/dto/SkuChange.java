package com.zjn.mall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张健宁
 * @ClassName SkuChange
 * @Description sku库存变更对象
 * @createTime 2024年09月20日 16:28:00
 */
@ApiModel("sku库存变更对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkuChange {
    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("购买数量")
    private Integer count;
}
