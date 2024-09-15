package com.zjn.mall;

import com.zjn.mall.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 张健宁
 * @ClassName SearchServiceApplication
 * @Description 搜索服务启动类
 * @createTime 2024年09月15日 21:53:00
 */

@SpringBootApplication
@MapperScan("com.zjn.mall.mapper")
@EnableFeignClients(basePackages = "com.zjn.mall.feign",
        defaultConfiguration = {DefaultFeignConfig.class})
public class SearchServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApplication.class, args);
    }
}
