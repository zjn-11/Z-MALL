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
 * @ClassName MemberAddr
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 用户配送地址
 */
@Schema(description="用户配送地址")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "member_addr")
public class MemberAddr implements Serializable {
    /**
     * ID
     */
    @TableId(value = "addr_id", type = IdType.INPUT)
    @Schema(description="ID")
    private Long addrId;

    /**
     * 用户ID
     */
    @TableField(value = "open_id")
    @Schema(description="用户ID")
    private String openId;

    /**
     * 收货人
     */
    @TableField(value = "receiver")
    @Schema(description="收货人")
    private String receiver;

    /**
     * 省ID
     */
    @TableField(value = "province_id")
    @Schema(description="省ID")
    private Long provinceId;

    /**
     * 省
     */
    @TableField(value = "province")
    @Schema(description="省")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    @Schema(description="城市")
    private String city;

    /**
     * 城市ID
     */
    @TableField(value = "city_id")
    @Schema(description="城市ID")
    private Long cityId;

    /**
     * 区
     */
    @TableField(value = "area")
    @Schema(description="区")
    private String area;

    /**
     * 区ID
     */
    @TableField(value = "area_id")
    @Schema(description="区ID")
    private Long areaId;

    /**
     * 邮编
     */
    @TableField(value = "post_code")
    @Schema(description="邮编")
    private String postCode;

    /**
     * 地址
     */
    @TableField(value = "addr")
    @Schema(description="地址")
    private String addr;

    /**
     * 手机
     */
    @TableField(value = "mobile")
    @Schema(description="手机")
    private String mobile;

    /**
     * 状态,1正常，0无效
     */
    @TableField(value = "`status`")
    @Schema(description="状态,1正常，0无效")
    private Integer status;

    /**
     * 是否默认地址 1是
     */
    @TableField(value = "common_addr")
    @Schema(description="是否默认地址 1是")
    private Integer commonAddr;

    /**
     * 建立时间
     */
    @TableField(value = "create_time")
    @Schema(description="建立时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @Schema(description="更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}