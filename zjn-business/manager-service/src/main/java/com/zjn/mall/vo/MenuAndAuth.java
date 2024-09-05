package com.zjn.mall.vo;

import com.zjn.mall.domain.SysMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author 张健宁
 * @ClassName MenuAndAuth
 * @Description 用户的菜单和操作权限集合对象
 * @createTime 2024年09月05日 15:20:00
 */

@ApiModel("用户的菜单和操作权限集合对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuAndAuth {
    @ApiModelProperty("菜单权限集合")
    private Set<SysMenu> menuList;
    @ApiModelProperty("操作权限集合")
    private Set<String> authorities;
}
