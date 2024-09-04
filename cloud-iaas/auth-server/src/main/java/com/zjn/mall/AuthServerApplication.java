package com.zjn.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 张健宁
 * @ClassName AuthServerApplication
 * @Description 认证授权服务启动类
 * @createTime 2024年09月03日 18:53:00
 */

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.zjn.mall.mapper")
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
