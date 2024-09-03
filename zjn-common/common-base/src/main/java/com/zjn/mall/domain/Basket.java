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
 * @ClassName Basket
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 购物车
 */
@Schema(description="购物车")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "basket")
public class Basket implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "basket_id", type = IdType.INPUT)
    @Schema(description="主键")
    private Long basketId;

    /**
     * 店铺ID
     */
    @TableField(value = "shop_id")
    @Schema(description="店铺ID")
    private Long shopId;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    @Schema(description="产品ID")
    private Long prodId;

    /**
     * SkuID
     */
    @TableField(value = "sku_id")
    @Schema(description="SkuID")
    private Long skuId;

    /**
     * 用户ID
     */
    @TableField(value = "open_id")
    @Schema(description="用户ID")
    private String openId;

    /**
     * 购物车产品个数
     */
    @TableField(value = "prod_count")
    @Schema(description="购物车产品个数")
    private Integer prodCount;

    /**
     * 购物时间
     */
    @TableField(value = "create_time")
    @Schema(description="购物时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}