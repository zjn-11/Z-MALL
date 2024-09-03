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
 * @ClassName Prod
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 商品
 */
@Schema(description="商品")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "prod")
public class Prod implements Serializable {
    /**
     * 产品ID
     */
    @TableId(value = "prod_id", type = IdType.INPUT)
    @Schema(description="产品ID")
    private Long prodId;

    /**
     * 商品名称
     */
    @TableField(value = "prod_name")
    @Schema(description="商品名称")
    private String prodName;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @Schema(description="店铺id")
    private Long shopId;

    /**
     * 原价
     */
    @TableField(value = "ori_price")
    @Schema(description="原价")
    private BigDecimal oriPrice;

    /**
     * 现价
     */
    @TableField(value = "price")
    @Schema(description="现价")
    private BigDecimal price;

    /**
     * 简要描述,卖点等
     */
    @TableField(value = "brief")
    @Schema(description="简要描述,卖点等")
    private String brief;

    /**
     * 详细描述
     */
    @TableField(value = "content")
    @Schema(description="详细描述")
    private String content;

    /**
     * 商品主图
     */
    @TableField(value = "pic")
    @Schema(description="商品主图")
    private String pic;

    /**
     * 商品图片，以,分割
     */
    @TableField(value = "imgs")
    @Schema(description="商品图片，以,分割")
    private String imgs;

    /**
     * 默认是1，表示正常状态, -1表示删除, 0下架
     */
    @TableField(value = "`status`")
    @Schema(description="默认是1，表示正常状态, -1表示删除, 0下架")
    private Integer status;

    /**
     * 商品分类
     */
    @TableField(value = "category_id")
    @Schema(description="商品分类")
    private Long categoryId;

    /**
     * 销量
     */
    @TableField(value = "sold_num")
    @Schema(description="销量")
    private Integer soldNum;

    /**
     * 总库存
     */
    @TableField(value = "total_stocks")
    @Schema(description="总库存")
    private Integer totalStocks;

    /**
     * 配送方式json见TransportModeVO
     */
    @TableField(value = "delivery_mode")
    @Schema(description="配送方式json见TransportModeVO")
    private String deliveryMode;

    /**
     * 运费模板id
     */
    @TableField(value = "delivery_template_id")
    @Schema(description="运费模板id")
    private Long deliveryTemplateId;

    /**
     * 录入时间
     */
    @TableField(value = "create_time")
    @Schema(description="录入时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @Schema(description="修改时间")
    private Date updateTime;

    /**
     * 上架时间
     */
    @TableField(value = "putaway_time")
    @Schema(description="上架时间")
    private Date putawayTime;

    /**
     * 版本 乐观锁
     */
    @TableField(value = "version")
    @Schema(description="版本 乐观锁")
    private Integer version;

    private static final long serialVersionUID = 1L;
}