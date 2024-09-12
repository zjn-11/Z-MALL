package com.zjn.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author 张健宁
 * @ClassName OrderServiceApplication
 * @Description 后台订单管理控制层
 * @createTime 2024年09月12日 16:29:00
 */

@SpringBootApplication
@MapperScan("com.zjn.mall.mapper")
@EnableCaching
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
