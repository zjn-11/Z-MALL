package com.zjn.mall.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.BasketMapper;
import com.zjn.mall.domain.Basket;
import com.zjn.mall.service.BasketService;
/**
 * @ClassName BasketServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月19日 10:03:00
 */

@Service
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket> implements BasketService{

}
