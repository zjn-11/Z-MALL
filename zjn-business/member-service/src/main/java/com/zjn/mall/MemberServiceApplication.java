package com.zjn.mall;

import com.zjn.mall.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 张健宁
 * @ClassName MemberServiceApplication
 * @Description 会员管理控制层
 * @createTime 2024年09月12日 12:32:00
 */

@SpringBootApplication
@MapperScan("com.zjn.mall.mapper")
@EnableCaching
@EnableFeignClients(basePackages = "com.zjn.mall.feign",
        defaultConfiguration = {DefaultFeignConfig.class})
public class MemberServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }
}
