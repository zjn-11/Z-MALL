package com.zjn.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.domain.*;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.feign.ProductClient;
import com.zjn.mall.model.Result;
import com.zjn.mall.util.AuthUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.BasketMapper;
import com.zjn.mall.service.BasketService;

/**
 * @author 张健宁
 * @ClassName BasketServiceImpl
 * @Description TODO
 * @createTime 2024年09月19日 10:03:00
 */

@Service
@RequiredArgsConstructor
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket> implements BasketService {

    private final BasketMapper basketMapper;
    private final ProductClient productClient;

    /**
     * 查询购物车不同商品数量
     *
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
     *
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

    /**
     * 计算会员所选商品价格：包括总额、优惠、合集、运费
     * 商品总金额：高于99元免运费，否则需要六元运费
     * @param shopCartIds
     * @return
     */
    @Override
    public CartTotalAmount querySelectedProdPriceByShopIds(List<Long> shopCartIds) {
        CartTotalAmount cartTotalAmount = new CartTotalAmount();
        // 购物车为空，则直接返回
        if (CollUtil.isEmpty(shopCartIds)) {
            return cartTotalAmount;
        }

        // 先根据id查出购物车记录，在根据记录的sku查出对应商品的sku信息
        List<Basket> basketList = basketMapper.selectBatchIds(shopCartIds);
        if (CollUtil.isEmpty(basketList)) {
            return cartTotalAmount;
        }
        List<Long> skuIds = basketList.stream()
                .map(Basket::getSkuId)
                .collect(Collectors.toList());
        Result<List<Sku>> skuResult = productClient.getSkuListBySkuIds(skuIds);
        if (skuResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign：获取商品sku信息失败，请重试！！！");
        }
        List<Sku> skuList = skuResult.getData();

        // 根据查询到的sku信息，计算总值
        List<BigDecimal> totalMoneyList = new ArrayList<>();
        List<BigDecimal> finalMoneyList = new ArrayList<>();

        basketList.forEach(basket -> {
            Sku skuTemp = skuList.stream()
                    .filter(sku -> sku.getSkuId().equals(basket.getSkuId()))
                    .collect(Collectors.toList()).get(0);
            Integer prodCount = basket.getProdCount();

            totalMoneyList.add(skuTemp.getOriPrice().multiply(new BigDecimal(prodCount)));
            finalMoneyList.add(skuTemp.getPrice().multiply(new BigDecimal(prodCount)));

        });
        BigDecimal totalMoney = totalMoneyList.stream().reduce(BigDecimal::add).get();
        BigDecimal finalMoney = finalMoneyList.stream().reduce(BigDecimal::add).get();

        cartTotalAmount.setTotalMoney(totalMoney);
        cartTotalAmount.setFinalMoney(finalMoney);

        // 计算优惠金额
        BigDecimal subtractMoney = totalMoney.subtract(finalMoney);
        cartTotalAmount.setSubtractMoney(subtractMoney);

        // 计算运费
        if (totalMoney.compareTo(new BigDecimal(99)) < 0) {
            BigDecimal transMoney = new BigDecimal(6);
            cartTotalAmount.setTransMoney(transMoney);
            cartTotalAmount.setFinalMoney(finalMoney.add(transMoney));
        }

        return cartTotalAmount;
    }

    /**
     * 购物车添加商品或修改商品数量
     * 商品详情页面点击添加购物车：如果已经存在，则是修改，否则就是新增
     * 购物车页面点击添加或减少：传入的prodCount为正数即为添加，负数则为减少
     * @param basket
     * @return
     */
    @Override
    public Boolean modifyBasketProdCount(Basket basket) {
        String openid = AuthUtils.getLoginMemberOpenid();
        // 现在购物车查询是否存在该商品
        Basket wxBasket = basketMapper.selectOne(
                new LambdaQueryWrapper<Basket>()
                        .eq(Basket::getOpenId, openid)
                        .eq(Basket::getSkuId, basket.getSkuId())
        );
        if (ObjectUtil.isNotNull(wxBasket)) {
            // 如果不为空，则说明购物车中已经存在该商品，直接修改数量
            int prodCount = wxBasket.getProdCount() + basket.getProdCount();
            if (prodCount < 0) {
                throw new BusinessException("购物车商品数量不能小于0！！！");
            }
            wxBasket.setProdCount(prodCount);
            return basketMapper.updateById(wxBasket) > 0;
        }
        // 如果为空，则需要添加
        basket.setCreateTime(new Date());
        basket.setOpenId(openid);
        return basketMapper.insert(basket) > 0;
    }
}
