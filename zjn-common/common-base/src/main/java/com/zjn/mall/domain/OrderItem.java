package com.zjn.mall.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName OrderItem
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 订单项
 */
@Schema(description="订单项")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "order_item")
public class OrderItem implements Serializable {
    /**
     * 订单项ID
     */
    @TableId(value = "order_item_id", type = IdType.INPUT)
    @Schema(description="订单项ID")
    private Long orderItemId;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @Schema(description="店铺id")
    private Long shopId;

    /**
     * 订单order_number
     */
    @TableField(value = "order_number")
    @Schema(description="订单order_number")
    private String orderNumber;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    @Schema(description="产品ID")
    private Long prodId;

    /**
     * 产品SkuID
     */
    @TableField(value = "sku_id")
    @Schema(description="产品SkuID")
    private Long skuId;

    /**
     * 购物车产品个数
     */
    @TableField(value = "prod_count")
    @Schema(description="购物车产品个数")
    private Integer prodCount;

    /**
     * 产品名称
     */
    @TableField(value = "prod_name")
    @Schema(description="产品名称")
    private String prodName;

    /**
     * sku名称
     */
    @TableField(value = "sku_name")
    @Schema(description="sku名称")
    private String skuName;

    /**
     * 产品主图片路径
     */
    @TableField(value = "pic")
    @Schema(description="产品主图片路径")
    private String pic;

    /**
     * 产品价格
     */
    @TableField(value = "price")
    @Schema(description="产品价格")
    private BigDecimal price;

    /**
     * 商品总金额
     */
    @TableField(value = "product_total_amount")
    @Schema(description="商品总金额")
    private BigDecimal productTotalAmount;

    /**
     * 购物时间
     */
    @TableField(value = "create_time")
    @Schema(description="购物时间")
    private Date createTime;

    /**
     * 评论状态： 0 未评价  1 已评价
     */
    @TableField(value = "comm_sts")
    @Schema(description="评论状态： 0 未评价  1 已评价")
    private Integer commSts;

    private static final long serialVersionUID = 1L;
}