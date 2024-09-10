package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.zjn.mall.domain.ProdTag;
import com.zjn.mall.domain.ProdTagReference;
import com.zjn.mall.domain.Sku;
import com.zjn.mall.service.ProdTagReferenceService;
import com.zjn.mall.service.ProdTagService;
import com.zjn.mall.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.ProdMapper;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.service.ProdService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName ProdServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

@Service
@RequiredArgsConstructor
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService{
    private final ProdMapper prodMapper;
    private final ProdTagReferenceService prodTagReferenceService;
    private final SkuService skuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveProd(Prod prod) {
        // 需要先新增商品得到商品id
        prod.setCreateTime(new Date());
        if (prod.getStatus().equals(1))
            prod.setPutawayTime(new Date());
        prod.setUpdateTime(new Date());
        prod.setShopId(1L);
        prod.setSoldNum(0);
        prod.setVersion(0);
        prod.setDeliveryMode(JSONObject.toJSONString(prod.getDeliveryModeVo()));
        int saveProd = prodMapper.insert(prod);
        if (saveProd > 0) {
            Long prodId = prod.getProdId();

            // 处理商品和标签关系prod_tag_reference
            List<Long> tagIdList = prod.getTagList();
            if (CollectionUtil.isNotEmpty(tagIdList)) {
                List<ProdTagReference> prodTagReferenceList = new ArrayList<>();
                tagIdList.forEach(tagId -> {
                    ProdTagReference prodTagReference = new ProdTagReference();
                    prodTagReference.setProdId(prodId);
                    prodTagReference.setTagId(tagId);
                    prodTagReference.setCreateTime(new Date());
                    prodTagReference.setShopId(prod.getShopId());
                    prodTagReference.setStatus(1);
                    prodTagReferenceList.add(prodTagReference);
                });
                prodTagReferenceService.saveBatch(prodTagReferenceList);
            }

            // 处理商品和sku关系
            List<Sku> skuList = prod.getSkuList();
            if (CollectionUtil.isNotEmpty(skuList)) {
                skuList.forEach(sku -> {
                    sku.setProdId(prodId);
                    sku.setCreateTime(new Date());
                    sku.setStatus(1);
                    sku.setUpdateTime(new Date());
                    sku.setVersion(1);
                    sku.setActualStocks(sku.getStocks());
                });
                skuService.saveBatch(skuList);
            }
        }

        return saveProd > 0;
    }
}
