package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.domain.ProdTag;
import com.zjn.mall.domain.ProdTagReference;
import com.zjn.mall.domain.Sku;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.service.ProdTagReferenceService;
import com.zjn.mall.service.ProdTagService;
import com.zjn.mall.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 新增商品信息
     * @param prod
     * @return
     */
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
            // 新增商品和标签关系
            saveProdTagReference(prod);

            // 新增商品sku信息
            saveProdSku(prod);
        }
        return saveProd > 0;
    }

    /**
     * 根据id查询商品信息
     * @param id
     * @return
     */
    @Override
    public Prod queryProdInfoById(Long id) {
        Prod prod = prodMapper.selectById(id);
        if (ObjectUtil.isNotEmpty(prod)) {
            // 获取tag标签
            List<Long> tagIdList = prodTagReferenceService.list(
                    new LambdaQueryWrapper<ProdTagReference>()
                            .eq(ProdTagReference::getProdId, id)
            ).stream().map(ProdTagReference::getTagId).collect(Collectors.toList());

            // 获取sku集合
            List<Sku> skuList = skuService.list(
                    new LambdaQueryWrapper<Sku>()
                            .eq(Sku::getProdId, id)
            );

            prod.setTagList(tagIdList);
            prod.setSkuList(skuList);
        }
        return prod;
    }

    /**
     * 修改商品信息
     * @param prod
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyProdInfo(Prod prod) {
        // 先删除tag和sku信息
        removeTagAndSku(prod.getProdId());
        // 新增tag关系
        saveProdTagReference(prod);
        // 新增sku信息
        saveProdSku(prod);
        return prodMapper.updateById(prod) > 0;
    }

    /**
     * 根据商品id删除商品信息
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeProdById(Long id) {
        removeTagAndSku(id);
        return prodMapper.deleteById(id) > 0;
    }

    /**
     * 新增商品和分组标签关系
     * @param prod
     */
    private void saveProdTagReference(Prod prod) {
        Long prodId = prod.getProdId();
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
    }

    /**
     * 新增商品sku信息
     * @param prod
     */
    private void saveProdSku(Prod prod) {
        Long prodId = prod.getProdId();
        List<Sku> skuList = prod.getSkuList();
        if (CollectionUtil.isEmpty(skuList)) {
            throw new BusinessException("商品sku信息不能为空!!!");
        }
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

    /**
     * 删除商品的tag和sku相关信息
     * @param prodId
     */
    private void removeTagAndSku(Long prodId) {
        prodTagReferenceService.remove(
                new LambdaQueryWrapper<ProdTagReference>()
                        .eq(ProdTagReference::getProdId, prodId)
        );
        skuService.remove(
                new LambdaQueryWrapper<Sku>()
                        .eq(Sku::getProdId, prodId)
        );
    }
}
