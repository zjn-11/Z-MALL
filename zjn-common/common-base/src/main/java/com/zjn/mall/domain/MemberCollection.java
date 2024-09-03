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
 * @ClassName MemberCollection
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

@Schema
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "member_collection")
public class MemberCollection implements Serializable {
    /**
     * 收藏表
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Schema(description="收藏表")
    private Long id;

    /**
     * 商品id
     */
    @TableField(value = "prod_id")
    @Schema(description="商品id")
    private Long prodId;

    /**
     * 用户id
     */
    @TableField(value = "open_id")
    @Schema(description="用户id")
    private String openId;

    /**
     * 收藏时间
     */
    @TableField(value = "create_time")
    @Schema(description="收藏时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}