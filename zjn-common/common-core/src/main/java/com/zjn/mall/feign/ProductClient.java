package com.zjn.mall.feign;

import com.zjn.mall.config.FeignInterceptor;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.feign.sentinel.ProductClientSentinel;
import com.zjn.mall.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName ProductClient
 * @Description 商品服务的feign接口
 * @createTime 2024年09月11日 23:49:00
 */

@FeignClient(value = "product-service", configuration = {FeignInterceptor.class},
        fallback = ProductClientSentinel.class)
public interface ProductClient {

    @GetMapping("prod/prod/getProdListByIds")
    Result<List<Prod>> loadProdInfoById(@RequestParam List<Long> prodIdList);
}
