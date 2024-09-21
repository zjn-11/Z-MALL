package com.zjn.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.domain.Basket;
import com.zjn.mall.dto.CartTotalAmount;
import com.zjn.mall.dto.CartVo;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.BasketService;
import com.zjn.mall.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @ApiOperation("购物车添加商品或修改商品数量")
    @PostMapping("changeItem")
    public Result<String> modifyBasketProdCount(@RequestBody Basket basket) {
        Boolean modify = basketService.modifyBasketProdCount(basket);
        return Result.handle(modify);
    }

    @ApiOperation("批量删除购物车记录")
    @DeleteMapping("deleteItem")
    public Result<String> removeBasketItemByBasketIds(@RequestBody List<Long> basketIds) {
        boolean delete = basketService.removeByIds(basketIds);
        return Result.handle(delete);
    }

    @ApiOperation("通过购物车id查询CartVo对象")
    @GetMapping("getCartVoByBasketIds")
    public Result<CartVo> getCartVoByBasketIds(@RequestParam List<Long> basketIds) {
        CartVo cartVo = basketService.getCartVoByBasketIds(basketIds);
        return Result.success(cartVo);
    }

    @ApiOperation("通过openid以及skuId集合删除购物车对象")
    @DeleteMapping("removeBasketsByOpenidAndSkuId")
    public Result<String> removeBasketsByOpenidAndSkuId(@RequestBody Map<String, Object> param) {
        String openid = (String) param.get("openid");
        List<Long> skuIds = (List<Long>) param.get("skuIds");
        boolean remove = basketService.remove(
                new LambdaQueryWrapper<Basket>()
                        .eq(Basket::getOpenId, openid)
                        .in(Basket::getSkuId, skuIds)
        );
        return Result.handle(remove);
    }
}
