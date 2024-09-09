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
 * @ClassName ProdTag
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 商品分组表
 */
@Schema(description="商品分组表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "prod_tag")
public class ProdTag implements Serializable {
    /**
     * 分组标签id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description="分组标签id")
    private Long id;

    /**
     * 分组标题
     */
    @TableField(value = "title")
    @Schema(description="分组标题")
    private String title;

    /**
     * 状态(1为正常,0为删除)
     */
    @TableField(value = "`status`")
    @Schema(description="状态(1为正常,0为删除)")
    private Integer status;

    /**
     * 列表样式(0:一列一个,1:一列两个,2:一列三个)
     */
    @TableField(value = "`style`")
    @Schema(description="列表样式(0:一列一个,1:一列两个,2:一列三个)")
    private Integer style;

    /**
     * 排序
     */
    @TableField(value = "seq")
    @Schema(description="排序")
    private Integer seq;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Schema(description="创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @Schema(description="修改时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}