package com.zjn.mall.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ProdTagReference
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

@Schema
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "prod_tag_reference")
public class ProdTagReference implements Serializable {
    /**
     * 分组引用id
     */
    @TableId(value = "reference_id", type = IdType.AUTO)
    @Schema(description="分组引用id")
    private Long referenceId;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @Schema(description="店铺id")
    private Long shopId;

    /**
     * 标签id
     */
    @TableField(value = "tag_id")
    @Schema(description="标签id")
    private Long tagId;

    /**
     * 商品id
     */
    @TableField(value = "prod_id")
    @Schema(description="商品id")
    private Long prodId;

    /**
     * 状态(1:正常,0:删除)
     */
    @TableField(value = "`status`")
    @Schema(description="状态(1:正常,0:删除)")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Schema(description="创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}