package com.zjn.mall.feign;

import com.zjn.mall.config.DefaultFeignConfig;
import com.zjn.mall.config.FeignInterceptor;
import com.zjn.mall.dto.CartTotalAmount;
import com.zjn.mall.dto.CartVo;
import com.zjn.mall.feign.sentinel.CartClientSentinel;
import com.zjn.mall.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName CartClient
 * @Description TODO
 * @createTime 2024年09月20日 14:15:00
 */
@FeignClient(value = "cart-service", configuration = {DefaultFeignConfig.class, FeignInterceptor.class},
        fallback = CartClientSentinel.class)
public interface CartClient {
    @GetMapping("/p/shopCart/getCartVoByBasketIds")
    Result<CartVo> getCartVoByBasketIds(@RequestParam List<Long> basketIds);

    @PostMapping("/p/shopCart/totalPay")
    Result<CartTotalAmount> loadSelectedProdPriceByShopIds(@RequestBody List<Long> shopCartIds);
}
