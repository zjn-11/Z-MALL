package com.zjn.mall.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Area
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

@Schema(description = "area")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "area")
public class Area implements Serializable {
    @TableId(value = "area_id", type = IdType.AUTO)
    @Schema(description="")
    private Long areaId;

    @TableField(value = "area_name")
    @Schema(description="")
    private String areaName;

    @TableField(value = "parent_id")
    @Schema(description="")
    private Long parentId;

    @TableField(value = "`level`")
    @Schema(description="")
    private Integer level;

    private static final long serialVersionUID = 1L;
}