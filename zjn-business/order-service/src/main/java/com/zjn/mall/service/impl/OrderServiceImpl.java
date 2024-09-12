package com.zjn.mall.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.Order;
import com.zjn.mall.mapper.OrderMapper;
import com.zjn.mall.service.OrderService;
/**
 * @ClassName OrderServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 16:32:00
 */

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService{

}
