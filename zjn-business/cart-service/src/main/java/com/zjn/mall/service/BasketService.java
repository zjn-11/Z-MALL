package com.zjn.mall.service;

import com.zjn.mall.domain.Basket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjn.mall.dto.CartTotalAmount;
import com.zjn.mall.dto.CartVo;

import java.util.List;

/**
 * @ClassName BasketService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月19日 10:03:00
 */

public interface BasketService extends IService<Basket>{


    Integer queryWxBasketProdCount(String openid);

    CartVo queryWxCartVoInfo(String openid);

    CartTotalAmount querySelectedProdPriceByShopIds(List<Long> shopCartIds);

    Boolean modifyBasketProdCount(Basket basket);

    CartVo getCartVoByBasketIds(List<Long> basketIds);
}
