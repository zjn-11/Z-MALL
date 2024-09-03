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
 * @ClassName Order
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 订单表
 */
@Schema(description="订单表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "`order`")
public class Order implements Serializable {
    /**
     * 订单ID
     */
    @TableId(value = "order_id", type = IdType.INPUT)
    @Schema(description="订单ID")
    private Long orderId;

    /**
     * 订购用户ID
     */
    @TableField(value = "open_id")
    @Schema(description="订购用户ID")
    private String openId;

    /**
     * 订购流水号
     */
    @TableField(value = "order_number")
    @Schema(description="订购流水号")
    private String orderNumber;

    /**
     * 总值
     */
    @TableField(value = "total_money")
    @Schema(description="总值")
    private BigDecimal totalMoney;

    /**
     * 实际总值
     */
    @TableField(value = "actual_total")
    @Schema(description="实际总值")
    private BigDecimal actualTotal;

    /**
     * 订单备注
     */
    @TableField(value = "remarks")
    @Schema(description="订单备注")
    private String remarks;

    /**
     * 订单状态 1:待付款 2:待发货 3:待收货 4:待评价 5:成功 6:失败
     */
    @TableField(value = "`status`")
    @Schema(description="订单状态 1:待付款 2:待发货 3:待收货 4:待评价 5:成功 6:失败")
    private Integer status;

    /**
     * 配送类型
     */
    @TableField(value = "dvy_type")
    @Schema(description="配送类型")
    private String dvyType;

    /**
     * 配送方式ID
     */
    @TableField(value = "dvy_id")
    @Schema(description="配送方式ID")
    private Long dvyId;

    /**
     * 物流单号
     */
    @TableField(value = "dvy_flow_id")
    @Schema(description="物流单号")
    private String dvyFlowId;

    /**
     * 订单运费
     */
    @TableField(value = "freight_amount")
    @Schema(description="订单运费")
    private BigDecimal freightAmount;

    /**
     * 用户订单地址Id
     */
    @TableField(value = "addr_order_id")
    @Schema(description="用户订单地址Id")
    private Long addrOrderId;

    /**
     * 订单商品总数
     */
    @TableField(value = "product_nums")
    @Schema(description="订单商品总数")
    private Integer productNums;

    /**
     * 订购时间
     */
    @TableField(value = "create_time")
    @Schema(description="订购时间")
    private Date createTime;

    /**
     * 订单更新时间
     */
    @TableField(value = "update_time")
    @Schema(description="订单更新时间")
    private Date updateTime;

    /**
     * 付款时间
     */
    @TableField(value = "pay_time")
    @Schema(description="付款时间")
    private Date payTime;

    /**
     * 发货时间
     */
    @TableField(value = "dvy_time")
    @Schema(description="发货时间")
    private Date dvyTime;

    /**
     * 完成时间
     */
    @TableField(value = "finally_time")
    @Schema(description="完成时间")
    private Date finallyTime;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    @Schema(description="取消时间")
    private Date cancelTime;

    /**
     * 是否已经支付，1：已经支付过，0：，没有支付过
     */
    @TableField(value = "is_payed")
    @Schema(description="是否已经支付，1：已经支付过，0：，没有支付过")
    private Integer isPayed;

    /**
     * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     */
    @TableField(value = "delete_status")
    @Schema(description="用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除")
    private Integer deleteStatus;

    /**
     * 0:默认,1:在处理,2:处理完成
     */
    @TableField(value = "refund_sts")
    @Schema(description="0:默认,1:在处理,2:处理完成")
    private Integer refundSts;

    /**
     * 优惠总额
     */
    @TableField(value = "reduce_amount")
    @Schema(description="优惠总额")
    private BigDecimal reduceAmount;

    /**
     * 订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易
     */
    @TableField(value = "close_type")
    @Schema(description="订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易")
    private Integer closeType;

    private static final long serialVersionUID = 1L;
}