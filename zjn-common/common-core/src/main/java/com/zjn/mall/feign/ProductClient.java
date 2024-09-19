package com.zjn.mall.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.config.DefaultFeignConfig;
import com.zjn.mall.config.FeignInterceptor;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.domain.ProdTagReference;
import com.zjn.mall.domain.Sku;
import com.zjn.mall.feign.sentinel.ProductClientSentinel;
import com.zjn.mall.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName ProductClient
 * @Description 商品服务的feign接口
 * @createTime 2024年09月11日 23:49:00
 */

@FeignClient(value = "product-service", configuration = {FeignInterceptor.class, DefaultFeignConfig.class},
        fallback = ProductClientSentinel.class)
public interface ProductClient {

    @GetMapping("prod/prod/getProdListByIds")
    Result<List<Prod>> loadProdInfoByIds(@RequestParam List<Long> prodIdList);

    @GetMapping("prod/prodTag/getProdTagReferencePageByTagId")
    Result<Page<ProdTagReference>> getProdTagReferencePageByTagId(@RequestParam Long current,
                                                                  @RequestParam Long size,
                                                                  @RequestParam Long tagId);

    @GetMapping("prod/category/getCategoryChildIdsByCategoryId")
    Result<List<Long>> getCategoryChildIdsByCategoryId(@RequestParam Long categoryId);

    @GetMapping("prod/prod/getProdListByCategoryIds")
    Result<List<Prod>> getProdListByCategoryIds(@RequestParam List<Long> categoryIds);

    @GetMapping("prod/prod/getSkuListBySkuIds")
    Result<List<Sku>> getSkuListBySkuIds(@RequestParam List<Long> skuIds);
}
