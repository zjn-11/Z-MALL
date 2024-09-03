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
 * @ClassName Member
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月03日 15:45:00
 */

/**
 * 用户表
 */
@Schema(description="用户表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "`member`")
public class Member implements Serializable {
    /**
     * 会员表的主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Schema(description="会员表的主键")
    private Integer id;

    /**
     * ID
     */
    @TableField(value = "open_id")
    @Schema(description="ID")
    private String openId;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    @Schema(description="用户昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    @Schema(description="真实姓名")
    private String realName;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_mail")
    @Schema(description="用户邮箱")
    private String userMail;

    /**
     * 支付密码
     */
    @TableField(value = "pay_password")
    @Schema(description="支付密码")
    private String payPassword;

    /**
     * 手机号码
     */
    @TableField(value = "user_mobile")
    @Schema(description="手机号码")
    private String userMobile;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @Schema(description="修改时间")
    private Date updateTime;

    /**
     * 注册时间
     */
    @TableField(value = "create_time")
    @Schema(description="注册时间")
    private Date createTime;

    /**
     * 注册IP
     */
    @TableField(value = "user_regip")
    @Schema(description="注册IP")
    private String userRegip;

    /**
     * 最后登录时间
     */
    @TableField(value = "user_lasttime")
    @Schema(description="最后登录时间")
    private Date userLasttime;

    /**
     * 最后登录IP
     */
    @TableField(value = "user_lastip")
    @Schema(description="最后登录IP")
    private String userLastip;

    /**
     * M(男) or F(女)
     */
    @TableField(value = "sex")
    @Schema(description="M(男) or F(女)")
    private String sex;

    /**
     * 例如：2009-11-27
     */
    @TableField(value = "birth_date")
    @Schema(description="例如：2009-11-27")
    private String birthDate;

    /**
     * 头像图片路径
     */
    @TableField(value = "pic")
    @Schema(description="头像图片路径")
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    @TableField(value = "`status`")
    @Schema(description="状态 1 正常 0 无效")
    private Integer status;

    /**
     * 用户积分
     */
    @TableField(value = "score")
    @Schema(description="用户积分")
    private Integer score;

    private static final long serialVersionUID = 1L;
}