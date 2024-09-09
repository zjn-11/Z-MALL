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
 * @ClassName Category
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 产品类目
 */
@Schema(description="产品类目")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "category")
public class Category implements Serializable {
    /**
     * 类目ID
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    @Schema(description="类目ID")
    private Long categoryId;

    /**
     * 父节点
     */
    @TableField(value = "parent_id")
    @Schema(description="父节点")
    private Long parentId;

    /**
     * 产品类目名称
     */
    @TableField(value = "category_name")
    @Schema(description="产品类目名称")
    private String categoryName;

    /**
     * 类目图标
     */
    @TableField(value = "icon")
    @Schema(description="类目图标")
    private String icon;

    /**
     * 类目的显示图片
     */
    @TableField(value = "pic")
    @Schema(description="类目的显示图片")
    private String pic;

    /**
     * 排序
     */
    @TableField(value = "seq")
    @Schema(description="排序")
    private Integer seq;

    /**
     * 默认是1，表示正常状态,0为下线状态
     */
    @TableField(value = "`status`")
    @Schema(description="默认是1，表示正常状态,0为下线状态")
    private Integer status;

    /**
     * 记录时间
     */
    @TableField(value = "create_time")
    @Schema(description="记录时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @Schema(description="更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}