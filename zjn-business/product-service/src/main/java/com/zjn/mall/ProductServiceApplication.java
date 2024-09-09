package com.zjn.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author 张健宁
 * @ClassName ProductServiceApplication
 * @Description 产品管理控制层
 * @createTime 2024年09月09日 22:16:00
 */

@SpringBootApplication
@MapperScan("com.zjn.mall.mapper")
@EnableCaching
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
