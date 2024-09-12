package com.zjn.mall.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.OrderItemMapper;
import com.zjn.mall.domain.OrderItem;
import com.zjn.mall.service.OrderItemService;
/**
 * @ClassName OrderItemServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 16:32:00
 */

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService{

}
