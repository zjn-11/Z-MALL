package com.zjn.mall.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张健宁
 * @ClassName UserAddrDto
 * @Description 小程序端用户收货地址DTO
 * @createTime 2024年09月14日 00:09:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "小程序端用户收货地址DTO")
public class UserAddrDto {
    /**
     * 收货人
     */
    @Schema(description="收货人")
    private String receiver;

    /**
     * 手机
     */
    @Schema(description="手机")
    private String mobile;

    /**
     * 省
     */
    @Schema(description="省")
    private String province;

    /**
     * 城市
     */
    @Schema(description="城市")
    private String city;

    /**
     * 区
     */
    @Schema(description="区")
    private String area;

    /**
     * 地址
     */
    @Schema(description="地址")
    private String addr;

}
