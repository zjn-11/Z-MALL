package com.zjn.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 张健宁
 * @ClassName ManagerServiceApplication
 * @Description 系统管理模块启动类
 * @createTime 2024年09月04日 23:51:00
 */
@SpringBootApplication
@MapperScan("com.zjn.mall.mapper")
public class ManagerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerServiceApplication.class, args);
    }
}
