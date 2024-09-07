package com.zjn.mall.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SysUser
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 系统用户
 */
@Schema(description="系统用户")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "sys_user")
public class SysUser implements Serializable {
    @TableId(value = "user_id", type = IdType.AUTO)
    @Schema(description="")
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @Schema(description="用户名")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "`password`", updateStrategy = FieldStrategy.NOT_EMPTY)
    @Schema(description="密码")
    private String password;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @Schema(description="邮箱")
    private String email;

    /**
     * 手机号
     */
    @TableField(value = "mobile")
    @Schema(description="手机号")
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    @TableField(value = "`status`")
    @Schema(description="状态  0：禁用   1：正常")
    private Integer status;

    /**
     * 创建者ID
     */
    @TableField(value = "create_user_id")
    @Schema(description="创建者ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Schema(description="创建时间")
    private Date createTime;

    /**
     * 用户所在的商城Id
     */
    @TableField(value = "shop_id")
    @Schema(description="用户所在的商城Id")
    private Long shopId;

    /**
     * 保存新增管理员时：用于存放角色id集合
     */
    @TableField(exist = false)
    @ApiModelProperty("角色id集合")
    private List<Long> roleIdList;

    private static final long serialVersionUID = 1L;
}