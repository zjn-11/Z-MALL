package com.zjn.mall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 张健宁
 * @ClassName ProdCommonViewDto
 * @Description 商品评论信息总览对象
 * @createTime 2024年09月16日 23:28:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品评论信息总览对象")
public class ProdCommonViewDto {

    @ApiModelProperty("商品评论总数量")
    private Integer allCount;
    @ApiModelProperty("商品评论好评数量")
    private Integer goodCount;
    @ApiModelProperty("商品评论好评率")
    private BigDecimal goodLv;
    @ApiModelProperty("商品评论中评数量")
    private Integer secondCount;
    @ApiModelProperty("商品评论差评数量")
    private Integer badCount;
    @ApiModelProperty("商品评论有图数量")
    private Integer picCount;
}
