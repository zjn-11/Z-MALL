package com.zjn.mall.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ProdProp
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

@Schema
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "prod_prop")
public class ProdProp implements Serializable {
    /**
     * 属性id
     */
    @TableId(value = "prop_id", type = IdType.AUTO)
    @Schema(description="属性id")
    private Long propId;

    /**
     * 属性名称
     */
    @TableField(value = "prop_name")
    @Schema(description="属性名称")
    private String propName;

    /**
     * ProdPropRule 1:销售属性(规格); 2:参数属性;
     */
    @TableField(value = "`rule`")
    @Schema(description="ProdPropRule 1:销售属性(规格); 2:参数属性;")
    private Integer rule;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @Schema(description="店铺id")
    private Long shopId;

    /**
     * 多条件分页查询时：需要展示的具体属性值
     */
    @TableField(exist = false)
    @ApiModelProperty("属性值集合")
    private List<ProdPropValue> prodPropValues;

    private static final long serialVersionUID = 1L;
}