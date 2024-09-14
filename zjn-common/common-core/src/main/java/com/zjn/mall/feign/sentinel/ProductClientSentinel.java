package com.zjn.mall.feign.sentinel;

import com.zjn.mall.domain.Prod;
import com.zjn.mall.feign.ProductClient;
import com.zjn.mall.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName ProductClientSentinel
 * @Description 商品模块远程接口的熔断机制
 * @createTime 2024年09月12日 00:02:00
 */
@Component
@Slf4j
public class ProductClientSentinel implements ProductClient {

    @Override
    public Result<List<Prod>> loadProdInfoByIds(List<Long> id) {
        log.error("根据多个id查询商品信息集合，接口调用失败！！！");
        return null;
    }
}
