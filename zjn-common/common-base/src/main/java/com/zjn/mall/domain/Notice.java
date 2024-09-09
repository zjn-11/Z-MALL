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
 * @ClassName Notice
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

@Schema
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "notice")
public class Notice implements Serializable {
    /**
     * 公告id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description="公告id")
    private Long id;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @Schema(description="店铺id")
    private Long shopId;

    /**
     * 公告标题
     */
    @TableField(value = "title")
    @Schema(description="公告标题")
    private String title;

    /**
     * 公告内容
     */
    @TableField(value = "content")
    @Schema(description="公告内容")
    private String content;

    /**
     * 状态(1:公布 0:撤回)
     */
    @TableField(value = "`status`")
    @Schema(description="状态(1:公布 0:撤回)")
    private Integer status;

    /**
     * 是否置顶
     */
    @TableField(value = "is_top")
    @Schema(description="是否置顶")
    private Integer isTop;

    /**
     * 发布时间
     */
    @TableField(value = "create_time")
    @Schema(description="发布时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @Schema(description="更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}