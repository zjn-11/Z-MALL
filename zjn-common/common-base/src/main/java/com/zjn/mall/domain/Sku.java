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
 * @ClassName Sku
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 单品SKU表
 */
@Schema(description="单品SKU表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "sku")
public class Sku implements Serializable {
    /**
     * 单品ID
     */
    @TableId(value = "sku_id", type = IdType.INPUT)
    @Schema(description="单品ID")
    private Long skuId;

    /**
     * 商品ID
     */
    @TableField(value = "prod_id")
    @Schema(description="商品ID")
    private Long prodId;

    /**
     * 销售属性组合字符串 格式是p1:v1;p2:v2
     */
    @TableField(value = "properties")
    @Schema(description="销售属性组合字符串 格式是p1:v1;p2:v2")
    private String properties;

    /**
     * 原价
     */
    @TableField(value = "ori_price")
    @Schema(description="原价")
    private BigDecimal oriPrice;

    /**
     * 价格
     */
    @TableField(value = "price")
    @Schema(description="价格")
    private BigDecimal price;

    /**
     * 商品在付款减库存的状态下，该sku上未付款的订单数量
     */
    @TableField(value = "stocks")
    @Schema(description="商品在付款减库存的状态下，该sku上未付款的订单数量")
    private Integer stocks;

    /**
     * 实际库存
     */
    @TableField(value = "actual_stocks")
    @Schema(description="实际库存")
    private Integer actualStocks;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @Schema(description="修改时间")
    private Date updateTime;

    /**
     * 记录时间
     */
    @TableField(value = "create_time")
    @Schema(description="记录时间")
    private Date createTime;

    /**
     * 商品条形码
     */
    @TableField(value = "model_id")
    @Schema(description="商品条形码")
    private String modelId;

    /**
     * sku图片
     */
    @TableField(value = "pic")
    @Schema(description="sku图片")
    private String pic;

    /**
     * sku名称
     */
    @TableField(value = "sku_name")
    @Schema(description="sku名称")
    private String skuName;

    /**
     * 商品名称
     */
    @TableField(value = "prod_name")
    @Schema(description="商品名称")
    private String prodName;

    /**
     * 版本号
     */
    @TableField(value = "version")
    @Schema(description="版本号")
    private Integer version;

    /**
     * 商品重量
     */
    @TableField(value = "weight")
    @Schema(description="商品重量")
    private Double weight;

    /**
     * 商品体积
     */
    @TableField(value = "volume")
    @Schema(description="商品体积")
    private Double volume;

    /**
     * 0 禁用 1 启用
     */
    @TableField(value = "`status`")
    @Schema(description="0 禁用 1 启用")
    private Integer status;

    private static final long serialVersionUID = 1L;
}