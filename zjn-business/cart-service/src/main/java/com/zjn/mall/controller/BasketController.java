package com.zjn.mall.controller;

import com.zjn.mall.domain.CartTotalAmount;
import com.zjn.mall.domain.CartVo;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.BasketService;
import com.zjn.mall.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName BasketController
 * @Description 购物车控制层
 * @createTime 2024年09月19日 10:15:00
 */
@Api("购物车控制层")
@RestController
@RequestMapping("p/shopCart")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @ApiOperation("小程序：获取购物车商品数量")
    @GetMapping("prodCount")
    public Result<Integer> loadWxBasketProdCount() {
        String openid = AuthUtils.getLoginMemberOpenid();
        Integer count = basketService.queryWxBasketProdCount(openid);
        return Result.success(count == null ? 0 : count);
    }

    @ApiOperation("小程序：查询购物车信息")
    @GetMapping("info")
    public Result<CartVo> loadWxCartInfo() {
        String openid = AuthUtils.getLoginMemberOpenid();
        CartVo cartVo = basketService.queryWxCartVoInfo(openid);
        return Result.success(cartVo);
    }

    @ApiOperation("计算会员所选商品价格：包括总额、优惠、合集、运费")
    @PostMapping("totalPay")
    public Result<CartTotalAmount> loadSelectedProdPriceByShopIds(@RequestBody List<Long> shopCartIds) {
        CartTotalAmount cartTotalAmount = basketService.querySelectedProdPriceByShopIds(shopCartIds);
        return Result.success(cartTotalAmount);
    }
}
