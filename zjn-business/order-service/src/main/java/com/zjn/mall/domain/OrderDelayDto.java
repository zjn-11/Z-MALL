package com.zjn.mall.domain;

import com.zjn.mall.dto.ChangeStock;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张健宁
 * @ClassName OrderDelayDto
 * @Description 订单延时传输对象
 * @createTime 2024年09月21日 20:09:00
 */
@ApiModel("订单延时传输对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDelayDto {

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("库存变更对象")
    private ChangeStock changeStock;

}
