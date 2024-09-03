package com.zjn.mall.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ProdPropValue
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

@Schema
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "prod_prop_value")
public class ProdPropValue implements Serializable {
    /**
     * 属性值ID
     */
    @TableId(value = "value_id", type = IdType.INPUT)
    @Schema(description="属性值ID")
    private Long valueId;

    /**
     * 属性值名称
     */
    @TableField(value = "prop_value")
    @Schema(description="属性值名称")
    private String propValue;

    /**
     * 属性ID
     */
    @TableField(value = "prop_id")
    @Schema(description="属性ID")
    private Long propId;

    private static final long serialVersionUID = 1L;
}