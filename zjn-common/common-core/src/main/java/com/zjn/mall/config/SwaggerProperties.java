package com.zjn.mall.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 张健宁
 * @ClassName SwaggerProperties
 * @Description Swagger公共配置
 * @createTime 2024年09月04日 13:21:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "swagger3")
public class SwaggerProperties {

    /**
     * 描述生成文档的包名
     */
    private String basePackage;

    /**
     * 作者名称
     */
    private String name;

    /**
     * 主页url
     */
    private String url;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 服务信息
     */
    private String license;

    /**
     * URL地址
     */
    private String licenseUrl;

    /**
     * 服务团队
     */
    private String termsOfServiceUrl;

    /**
     * 版本号
     */
    private String version;

}
