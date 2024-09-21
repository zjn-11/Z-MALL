package com.zjn.mall.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.domain.*;
import com.zjn.mall.dto.*;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.feign.CartClient;
import com.zjn.mall.feign.MemberClient;
import com.zjn.mall.feign.ProductClient;
import com.zjn.mall.mapper.OrderItemMapper;
import com.zjn.mall.mapper.OrderMapper;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.OrderItemService;
import com.zjn.mall.service.OrderService;
import com.zjn.mall.util.AuthUtils;
import com.zjn.mall.vo.OrderStatusCountVO;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Proc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author 张健宁
 * @ClassName OrderServiceImpl
 * @Description TODO
 * @createTime 2024年09月12日 16:32:00
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemService orderItemService;
    private final MemberClient memberClient;
    private final ProductClient productClient;
    private final CartClient cartClient;
    private final Snowflake snowflake;

    /**
     * 多条件分页查询订单
     * 可以查询所有状态的订单，包括已删除的订单
     *
     * @param orderPage
     * @param orderNumber
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Page<Order> queryOrderPage(Page<Order> orderPage, String orderNumber, Integer status, Date startTime, Date endTime) {
        // 多条件分页查询会员
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<Order>()
                .eq(ObjectUtil.isNotNull(status), Order::getStatus, status)
                .like(StringUtils.hasText(orderNumber), Order::getOrderNumber, orderNumber)
                .between(ObjectUtil.isAllNotEmpty(startTime, endTime), Order::getCreateTime, startTime, endTime)
                .orderByDesc(Order::getCreateTime);
        orderPage = page(orderPage, queryWrapper);
        // 后台根据订单号查询出订单详情
        String loginType = AuthConstants.SYS_USER_LOGIN;
        queryOrderItemByOrderNumber(orderPage, loginType);
        return orderPage;
    }

    /**
     * 根据订单号查询出详情信息
     *
     * @param orderNumber
     * @return
     */
    @Override
    public Order queryOrderDetailById(String orderNumber) {
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNumber, orderNumber)
        );

        if (ObjectUtil.isNotEmpty(order)) {
            // 查询订单详情信息
            List<OrderItem> orderItems = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>()
                            .eq(OrderItem::getOrderNumber, orderNumber)
            );
            order.setOrderItems(orderItems);

            // 查询地址详情信息
            Result<MemberAddr> result = memberClient.getMemberAddrById(order.getAddrOrderId());
            if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
                throw new BusinessException("Feign接口调用失败：未能获取到收货地址信息！");
            }
            order.setUserAddrOrder(result.getData());

            // 查询会员昵称
            Result<String> result1 = memberClient.getNickNameByOpenId(order.getOpenId());
            if (result1.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
                throw new BusinessException("Feign接口调用失败：未能获取到用户名称！");
            }
            order.setNickName(result1.getData());
        }
        return order;
    }

    /**
     * 获取不同状态订单数量
     * 1:待付款
     * 2:待发货
     * 3:待收货
     *
     * @return
     */
    @Override
    public OrderStatusCountVO queryOrderCountByStatus() {
        String openid = AuthUtils.getLoginMemberOpenid();
        Integer unPay = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 1)
        );
        Integer payed = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 2)
        );
        Integer consignment = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(Order::getStatus, 3)
        );
        return OrderStatusCountVO.builder()
                .unPay(unPay).payed(payed).consignment(consignment)
                .build();
    }

    /**
     * 多条件分页查询会员的订单
     * 需要排除掉被删除订单，即只需要deleteStatus = 0 的订单
     *
     * @param orderPage
     * @param status
     * @return
     */
    @Override
    public Page<Order> queryMemberOrderPage(Page<Order> orderPage, Integer status) {
        String openid = AuthUtils.getLoginMemberOpenid();
        // 多条件分页查询会员
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getOpenId, openid)
                .eq(!status.equals(0), Order::getStatus, status)
                .eq(Order::getDeleteStatus, 0)
                .orderByDesc(Order::getCreateTime);

        orderPage = page(orderPage, queryWrapper);

        // 小程序获取订单详情集合
        String loginType = AuthConstants.MEMBER_LOGIN;
        queryOrderItemByOrderNumber(orderPage, loginType);
        return orderPage;
    }

    /**
     * 获取订单详细信息
     *
     * @param orderNumber
     * @return
     */
    @Override
    public Order queryOrderDetailByOrderNumber(String orderNumber) {
        // 查询出订单信息
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNumber, orderNumber)
        );
        if (ObjectUtil.isEmpty(order)) {
            throw new BusinessException("订单编号有误，请联系平台核实！");
        }

        // 查询出地址信息
        Result<MemberAddr> result = memberClient.getMemberAddrById(order.getAddrOrderId());
        if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口调用失败：未能获取到收货地址信息！");
        }
        UserAddrDto userAddrDto = BeanUtil.toBean(result.getData(), UserAddrDto.class);
        order.setUserAddrDto(userAddrDto);

        // 查询订单条目信息
        List<OrderItem> orderItems = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderNumber, orderNumber)
        );
        order.setOrderItemDtos(orderItems);

        return order;
    }

    /**
     * 会员确认收货
     * 状态变为5：已完成
     *
     * @param orderNumber
     * @return
     */
    @Override
    public Boolean receiptMemberOrder(String orderNumber) {
        Order order = Order.builder()
                .finallyTime(new Date())
                .updateTime(new Date())
                .status(5)
                .build();
        return orderMapper.update(order,
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNumber, orderNumber)
        ) > 0;
    }

    /**
     * 根据订单号查询出订单详情
     *
     * @param orderPage
     */
    public void queryOrderItemByOrderNumber(Page<Order> orderPage, String loginType) {
        // 根据订单号查询出订单详情
        List<Order> orders = orderPage.getRecords();
        if (CollectionUtil.isNotEmpty(orders)) {
            List<String> orderNumbers = orders.stream().map(Order::getOrderNumber).collect(Collectors.toList());
            List<OrderItem> orderItems = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>()
                            .in(OrderItem::getOrderNumber, orderNumbers)
            );
            orders.forEach(order -> {
                List<OrderItem> orderItemList = orderItems.stream()
                        .filter(orderItem -> orderItem.getOrderNumber().equals(order.getOrderNumber()))
                        .collect(Collectors.toList());
                if (loginType.equals(AuthConstants.SYS_USER_LOGIN))
                    order.setOrderItems(orderItemList);
                else
                    order.setOrderItemDtos(orderItemList);
            });
        }
    }

    /**
     * 展示订单详情页面
     * OrderVo -> ShopOrder -> OrderItem
     * 订单展示页面 -> 里面有多个店铺 -> 每个店铺都有自己的商品条目信息
     *
     * @param orderConfirmVo
     * @return
     */
    @Override
    public OrderVo queryWxOrderVo(OrderConfirmVo orderConfirmVo) {
        OrderVo orderVo = new OrderVo();
        String openid = AuthUtils.getLoginMemberOpenid();
        // 查询会员默认收货地址
        Result<MemberAddr> addrResult = memberClient.getMemberDefaultAddrByOpenid(openid);
        if (addrResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口调用失败：未能获取到默认收货地址信息!");
        }
        MemberAddr defultMemberAddr = addrResult.getData();
        orderVo.setMemberAddr(defultMemberAddr);

        List<Long> basketIds = orderConfirmVo.getBasketIds();
        if (CollectionUtil.isEmpty(basketIds)) {
            // 如果为空则说明是来自订单详情页面的提交订单请求
            productToConfirm(orderConfirmVo.getOrderItem(), orderVo);
        } else {
            // 如果不为空则说明是来自购物车页面的提交订单请求
            orderVo.setSource(1);
            cartToConfirm(basketIds, orderVo);
        }
        return orderVo;
    }

    /**
     * 处理请求来自购物车页面的情况
     * 这种情况下会有多个店铺，每个店铺都有多个商品，前端传递的是购物车id集合
     *
     * @param basketIds
     * @param orderVo
     */
    private void cartToConfirm(List<Long> basketIds, OrderVo orderVo) {
        // 先通过basketIds查出所有的购物车对象
        Result<CartVo> cartVoResult = cartClient.getCartVoByBasketIds(basketIds);
        if (cartVoResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口调用失败：未能获取到购物车商品信息!");
        }
        CartVo cartVo = cartVoResult.getData();

        AtomicReference<Integer> totalCount = new AtomicReference<>(0);

        // 将cartVo的属性赋值给orderVo
        List<ShopCart> shopCarts = cartVo.getShopCarts();
        List<ShopOrder> shopOrders = new ArrayList<>();
        shopCarts.forEach(shopCart -> {
            ShopOrder shopOrder = new ShopOrder();
            ArrayList<OrderItem> orderItems = new ArrayList<>();

            List<CartItem> shopCartItems = shopCart.getShopCartItems();
            shopCartItems.forEach(shopcartItem -> {
                OrderItem orderItem = BeanUtil.copyProperties(shopcartItem, OrderItem.class);
                orderItem.setCreateTime(new Date());
                orderItem.setCommSts(0);
                orderItem.setShopId(shopCart.getShopId());
                // 计算实际总额
                BigDecimal finalPrice = shopcartItem.getPrice().multiply(new BigDecimal(shopcartItem.getProdCount()));
                orderItem.setProductTotalAmount(finalPrice);
                orderItems.add(orderItem);
                totalCount.updateAndGet(v -> v + shopcartItem.getProdCount());
            });

            shopOrder.setShopOrderItems(orderItems);
            shopOrder.setShopId(shopCart.getShopId());

            shopOrders.add(shopOrder);
        });

        // 根据id获取价格
        Result<CartTotalAmount> priceResult = cartClient.loadSelectedProdPriceByShopIds(basketIds);
        if (priceResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口调用失败：未能获取到购物车商品价格信息!");
        }
        CartTotalAmount cartTotalAmount = priceResult.getData();

        orderVo.setShopCartOrders(shopOrders);
        orderVo.setTransfee(cartTotalAmount.getTransMoney());
        orderVo.setTotal(cartTotalAmount.getTotalMoney());
        orderVo.setActualTotal(cartTotalAmount.getFinalMoney());
        orderVo.setShopReduce(cartTotalAmount.getSubtractMoney());
        orderVo.setTotalCount(totalCount.get());
    }

    /**
     * 处理请求来自商品详情页面的情况
     * 这种情况下只会有一个商品，前端会装入orderItem中传递回来
     *
     * @param orderItem
     * @param orderVo
     */
    private void productToConfirm(OrderItem orderItem, OrderVo orderVo) {
        ArrayList<ShopOrder> shopOrders = new ArrayList<>();
        ShopOrder shopOrder = new ShopOrder();
        ArrayList<OrderItem> orderItems = new ArrayList<>();

        // 查询出sku对象
        Long skuId = orderItem.getSkuId();
        Result<List<Sku>> skuResult = productClient.getSkuListBySkuIds(Collections.singletonList(skuId));
        if (skuResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口调用失败：未能获取到商品sku信息!");
        }
        Sku sku = skuResult.getData().get(0);

        // 购买商品的数量
        Integer prodCount = orderItem.getProdCount();
        // 计算总金额
        BigDecimal totalPrice = sku.getOriPrice().multiply(new BigDecimal(prodCount));
        // 计算实际总额
        BigDecimal finalPrice = sku.getPrice().multiply(new BigDecimal(prodCount));

        // 订单商品条目属性赋值
        orderItem.setCreateTime(new Date());
        orderItem.setCommSts(0);
        orderItem.setProductTotalAmount(finalPrice);
        BeanUtil.copyProperties(sku, orderItem);
        orderItems.add(orderItem);

        // 订单店铺对象属性赋值
        shopOrder.setShopId(orderItem.getShopId());
        shopOrder.setShopOrderItems(orderItems);
        shopOrders.add(shopOrder);

        // 订单展示对象属性赋值
        orderVo.setShopCartOrders(shopOrders);
        orderVo.setTotal(totalPrice);
        orderVo.setTotalCount(prodCount);
        BigDecimal shopReduce = totalPrice.subtract(finalPrice);
        orderVo.setShopReduce(shopReduce);
        if (finalPrice.compareTo(new BigDecimal(99)) < 0) {
            orderVo.setTransfee(new BigDecimal(6));
            orderVo.setActualTotal(finalPrice.add(new BigDecimal(6)));
        } else {
            orderVo.setActualTotal(finalPrice);
        }
    }

    /**
     * 会员提交订单:
     * 1 判断订单请求来源：
     * 1.1 来自购物车，需要删除购物车中对应信息
     * 1.2 来自商品详情页面
     * 2 修改prod和sku库存数量
     * 3 写订单
     * 3.1 写订单表order
     * 3.2 写订单条目表order_item
     * 4 通过消息机制实现超时未支付
     *
     * @param orderVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String submitOrder(OrderVo orderVo) {
        String openid = AuthUtils.getLoginMemberOpenid();
        // 移除购物车信息
        if (orderVo.getSource().equals(1)) {
            // 请求来自购物车
            // 删除会员选中的购物车商品信息
            clearMemberCheckedBasket(openid, orderVo);
        }

        // 扣件库存
        ChangeStock changeStock = getChangeStockObject(orderVo);
        // 将库存更新量变为负，实现扣减
        changeStock.getSkuChangeList().forEach(skuChange -> {
            skuChange.setCount(skuChange.getCount() * -1);
        });
        changeStock.getProdChangeList().forEach(prodChange -> {
            prodChange.setCount(prodChange.getCount() * -1);
        });
        changeStocksByChangeStock(changeStock);

        // 生成唯一的订单号
        String orderNumber = generateOrderNumber();
        // 保存订单order和order_item
        writOrder(orderVo, orderNumber, openid);
        writeOrderItems(orderVo, orderNumber);
        return orderNumber;
    }

    /**
     * 保存订单明细order_item
     * @param orderVo
     * @param orderNumber
     */
    private void writeOrderItems(OrderVo orderVo, String orderNumber) {
        List<ShopOrder> shopCartOrders = orderVo.getShopCartOrders();
        ArrayList<OrderItem> allOrderItem = new ArrayList<>();
        shopCartOrders.forEach(shopOrder -> {
            List<OrderItem> orderItems = shopOrder.getShopOrderItems();
            orderItems.forEach(orderItem -> {
                orderItem.setOrderNumber(orderNumber);
            });
            allOrderItem.addAll(orderItems);
        });
        if (!orderItemService.saveBatch(allOrderItem)) {
            throw new BusinessException("订单明细创建失败，请重试！");
        }
    }

    /**
     * 保存订单order
     * @param orderVo
     */
    private void writOrder(OrderVo orderVo, String orderNumber, String openid) {
        Order order = new Order();
        order.setOpenId(openid);
        order.setAddrOrderId(orderVo.getMemberAddr().getAddrId());
        order.setOrderNumber(orderNumber);
        order.setTotalMoney(orderVo.getTotal());
        order.setActualTotal(orderVo.getActualTotal());
        order.setRemarks(orderVo.getRemark());
        order.setStatus(1);
        order.setFreightAmount(orderVo.getTransfee());
        order.setProductNums(orderVo.getTotalCount());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setIsPayed(0);
        order.setDeleteStatus(0);
        order.setReduceAmount(orderVo.getShopReduce());
        if (orderMapper.insert(order) <= 0) {
            throw new BusinessException("订单创建失败，请重试！");
        }
    }

    /**
     * 使用雪花算法生成订单号
     * @return
     */
    private String generateOrderNumber() {
        return snowflake.nextIdStr();
    }

    /**
     * 删除会员购买所选的商品在购物车中的记录
     *
     * @param openid
     * @param orderVo
     */
    private void clearMemberCheckedBasket(String openid, OrderVo orderVo) {
        // 获取所有商品的sku集合
        ArrayList<Long> skuIdList = new ArrayList<>();
        List<ShopOrder> shopCartOrders = orderVo.getShopCartOrders();
        shopCartOrders.forEach(shopOrder -> {
            List<OrderItem> shopOrderItems = shopOrder.getShopOrderItems();
            List<Long> skuIds = shopOrderItems.stream().map(OrderItem::getSkuId).collect(Collectors.toList());
            skuIdList.addAll(skuIds);
        });
        Map<String, Object> param = new HashMap<>();
        param.put("openid", openid);
        param.put("skuIds", skuIdList);
        // 删除购物车对象
        Result<String> removeResult = cartClient.removeBasketsByOpenidAndSkuId(param);
        if (removeResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口调用失败：未能移除购物车商品信息信息!");
        }
    }

    /**
     * 获取商品库存数量变更对象
     * ChangeStock: SkuChangeList, ProdChangeList
     * SkuChange: skuId, skuCount
     * ProdChange: prodId, prodCount
     *
     * @param orderVo
     */
    private ChangeStock getChangeStockObject(OrderVo orderVo) {
        // 封装商品数量变化集合
        ArrayList<ProdChange> prodChanges = new ArrayList<>();
        ArrayList<SkuChange> skuChanges = new ArrayList<>();

        List<ShopOrder> shopCartOrders = orderVo.getShopCartOrders();
        shopCartOrders.forEach(shopOrder -> {
            List<OrderItem> orderItems = shopOrder.getShopOrderItems();
            orderItems.forEach(orderItem -> {
                SkuChange skuChange = new SkuChange();
                skuChange.setSkuId(orderItem.getSkuId());
                skuChange.setCount(orderItem.getProdCount());
                skuChanges.add(skuChange);

                // 要判断商品id之前是否出现过
                List<ProdChange> BeforeProdChange = prodChanges.stream().filter(prodChange1 -> prodChange1.getProdId().equals(orderItem.getProdId())).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(BeforeProdChange)) {
                    // 之前没有出现过
                    ProdChange prodChange = new ProdChange();
                    prodChange.setProdId(orderItem.getProdId());
                    prodChange.setCount(orderItem.getProdCount());
                    prodChanges.add(prodChange);
                } else {
                    ProdChange prodChange = BeforeProdChange.get(0);
                    int finalCount = prodChange.getCount() + orderItem.getProdCount();
                    prodChange.setCount(finalCount);
                }
            });
        });

        return new ChangeStock(skuChanges, prodChanges);
    }

    /**
     * 根据变更对象修改商品库存数量
     * @param changeStock
     */
    private void changeStocksByChangeStock(ChangeStock changeStock) {
        Result<Boolean> changeResult = productClient.changeProdAndSkuStock(changeStock);
        if (changeResult.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口调用失败：未能成功扣件库存!");
        }
        Boolean flag = changeResult.getData();
        if (!flag) {
            throw new BusinessException("未能成功扣件库存!");
        }
    }
}
