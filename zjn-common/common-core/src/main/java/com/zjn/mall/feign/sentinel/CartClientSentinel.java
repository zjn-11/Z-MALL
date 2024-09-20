package com.zjn.mall.feign.sentinel;

import com.zjn.mall.dto.CartTotalAmount;
import com.zjn.mall.dto.CartVo;
import com.zjn.mall.feign.CartClient;
import com.zjn.mall.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
