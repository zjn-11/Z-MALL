package com.zjn.mall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张健宁
 * @ClassName ProdChange
 * @Description Prod库存变更对象
 * @createTime 2024年09月20日 16:30:00
 */
@ApiModel("Prod库存变更对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdChange {
    @ApiModelProperty("prodId")
    private Long prodId;

    @ApiModelProperty("购买数量")
    private Integer count;
}
