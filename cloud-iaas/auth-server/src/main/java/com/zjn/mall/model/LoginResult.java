package com.zjn.mall.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张健宁
 * @ClassName LoginResult
 * @Description 登录统一结果对象
 * @createTime 2024年09月03日 23:14:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("登录统一结果对象")
public class LoginResult {

    @ApiModelProperty("令牌TOKEN")
    private String accessToken;

    @ApiModelProperty("有效时长")
    private Long expireIn;
}
