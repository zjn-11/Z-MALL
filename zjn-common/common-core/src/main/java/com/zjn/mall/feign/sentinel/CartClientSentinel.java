package com.zjn.mall.feign.sentinel;

import com.zjn.mall.dto.CartTotalAmount;
import com.zjn.mall.dto.CartVo;
import com.zjn.mall.feign.CartClient;
import com.zjn.mall.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author 张健宁
 * @ClassName CartClientSentinel
 * @Description TODO
 * @createTime 2024年09月20日 14:17:00
 */
@Component
@Slf4j
public class CartClientSentinel implements CartClient {
    @Override
    public Result<CartVo> getCartVoByBasketIds(List<Long> basketIds) {
        log.error("根据basketIds查询购物车页面展示对象，Feign接口调用失败！！！");
        return null;
    }

    @Override
    public Result<CartTotalAmount> loadSelectedProdPriceByShopIds(List<Long> shopCartIds) {
        log.error("根据basketIds获取金额对象，Feign接口调用失败！！！");
        return null;
    }

    @Override
    public Result<String> removeBasketsByOpenidAndSkuId(@RequestBody Map<String, Object> param) {
        log.error("通过openid以及skuId集合删除购物车对象，Feign接口调用失败！！！");
        return null;
    }
}
