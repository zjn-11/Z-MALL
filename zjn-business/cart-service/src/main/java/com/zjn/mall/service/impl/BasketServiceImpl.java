package com.zjn.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.domain.*;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.feign.ProductClient;
import com.zjn.mall.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.BasketMapper;
import com.zjn.mall.service.BasketService;
/**
 * @ClassName BasketServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月19日 10:03:00
 */

@Service
@RequiredArgsConstructor
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket> implements BasketService{

    private final BasketMapper basketMapper;
    private final ProductClient productClient;

    /**
     * 查询购物车不同商品数量
     * @param openid
     * @return
     */
    @Override
    public Integer queryWxBasketProdCount(String openid) {
        return basketMapper.selectCount(
                new LambdaQueryWrapper<Basket>()
                        .eq(Basket::getOpenId, openid)
        );
    }

    /**
     * 查询购物车商品信息
     * 涉及到三个对象信息：购物车展示对象 -> 购物车店铺对象 -> 购物车商品条目对象
     * CartVo -> ShopCart -> CartItem
     * @param openid
     * @return
     */
    @Override
    public CartVo queryWxCartVoInfo(String openid) {
        // 购物车展示对象集合
        CartVo cartVo = new CartVo();

        // 查询会员购物车记录
        List<Basket> basketList = basketMapper.selectList(
                new LambdaQueryWrapper<Basket>()
                        .eq(Basket::getOpenId, openid)
        );
        if (CollUtil.isEmpty(basketList)) {
            return cartVo;
        }

        // 获取购物车中所有的sku
        List<Long> skuIdList = basketList.stream().map(Basket::getSkuId).collect(Collectors.toList());
        Result<List<Sku>> skuResult = productClient.getSkuListBySkuIds(skuIdList);
        if (skuResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign：获取商品sku信息失败，请重试！！！");
        }
        List<Sku> skuList = skuResult.getData();

        // 购物车店铺对象集合
        List<ShopCart> shopCartList = new ArrayList<>();
        // 遍历购物车集合，查询出每一行对应的商品信息
        basketList.forEach(basket -> {
            // 判断当前购物车记录所属店铺是否存在于shopCartList
            List<ShopCart> isExistShop = shopCartList.stream()
                    .filter(shopCart -> shopCart.getShopId().equals(basket.getShopId()))
                    .collect(Collectors.toList());
            // 创建购物车商品条目信息，并为其赋值
            CartItem cartItem = new CartItem();

            cartItem.setBasketId(basket.getBasketId());
            cartItem.setProdCount(basket.getProdCount());

            Sku skuTemp = skuList.stream()
                    .filter(sku -> sku.getSkuId().equals(basket.getSkuId()))
                    .collect(Collectors.toList()).get(0);

            BeanUtil.copyProperties(skuTemp, cartItem);

            if (CollUtil.isEmpty(isExistShop)) {
                // 则证明该店铺之前没有出现过，所以需要单独创建一个shopCart对象
                // 创建购物车店铺对象
                ShopCart shopCart = new ShopCart();
                // 创建购物车商品条目对象集合
                ArrayList<CartItem> cartItemList = new ArrayList<>();
                // 将商品条目添加到集合中
                cartItemList.add(cartItem);
                // 将属性添加到购物车店铺对象中
                shopCart.setShopId(basket.getShopId());
                shopCart.setShopCartItems(cartItemList);
                // 将购物车店铺对象添加到列表集合中
                shopCartList.add(shopCart);
            } else {
                // 从之前创建好的购物车店铺对象集合中取出当前店铺对应的对象
                ShopCart shopCart = isExistShop.get(0);
                List<CartItem> cartItemList = shopCart.getShopCartItems();
                // 将商品条目添加到集合中
                cartItemList.add(cartItem);
            }
        });

        cartVo.setShopCarts(shopCartList);

        return cartVo;
    }
}
