package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.constants.ProductConstants;
import com.zjn.mall.domain.ProdPropValue;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.mapper.ProdPropValueMapper;
import com.zjn.mall.service.ProdPropValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.ProdPropMapper;
import com.zjn.mall.domain.ProdProp;
import com.zjn.mall.service.ProdPropService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @ClassName ProdPropServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "com.zjn.mall.service.impl.ProdPropServiceImpl")
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService{

    private final ProdPropMapper prodPropMapper;
    private final ProdPropValueMapper prodPropValueMapper;
    private final ProdPropValueService prodPropValueService;

    /**
     * 多条件分页查询商品属性
     * @param current
     * @param size
     * @param propName
     * @return
     */
    @Override
    public Page<ProdProp> queryProdSpecPage(Long current, Long size, String propName) {
        Page<ProdProp> prodPropPage = new Page<>(current, size);
        prodPropPage = prodPropMapper.selectPage(prodPropPage,
                new LambdaQueryWrapper<ProdProp>()
                        .like(StringUtils.hasText(propName), ProdProp::getPropName, propName)
        );
        // 从分页对象中获取属性值
        List<ProdProp> prodPropList = prodPropPage.getRecords();

        if (CollectionUtil.isEmpty(prodPropList)) {
            // 如果没有属性，则直接返回
            return prodPropPage;
        }
        // 如果属性集合不为空，则更具属性集合查询出对应的属性值集合
        /* 这个方法每一次循环都要查一次数据库，效率较低
        prodPropList.forEach(prodProp -> {
            List<ProdPropValue> propValueList = prodPropValueMapper.selectList(
                    new LambdaQueryWrapper<ProdPropValue>()
                            .eq(ProdPropValue::getPropId, prodProp.getPropId())
            );
            prodProp.setProdPropValues(propValueList);
        });
        */
        List<Long> propIdList = prodPropList.stream()
                                            .map(ProdProp::getPropId)
                                            .collect(Collectors.toList());
        List<ProdPropValue> prodPropValueList = prodPropValueMapper.selectList(
                new LambdaQueryWrapper<ProdPropValue>()
                        .in(ProdPropValue::getPropId, propIdList)
        );
        prodPropList.forEach(prodProp -> {
            List<ProdPropValue> propValues = prodPropValueList.stream()
                    .filter(prodPropValue -> prodPropValue.getPropId().equals(prodProp.getPropId()))
                    .collect(Collectors.toList());
            prodProp.setProdPropValues(propValues);
        });
        return prodPropPage;
    }

    /**
     * 新增商品规格
     * @param prodProp
     * @return
     */
    @Override
    @CacheEvict(key = ProductConstants.ALL_PROD_PROP_LIST_KEY)
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveProdSpec(ProdProp prodProp) {
        if (!StringUtils.hasText(prodProp.getPropName())) {
            throw new BusinessException("属性名不能为空，请重新添加!!!");
        }
        prodProp.setRule(2);
        prodProp.setShopId(1L);
        if (prodPropMapper.insert(prodProp) > 0) {
            return saveBatchProdPropValue(prodProp);
        }
         return false;
    }

    /**
     * 修改商品规格信息
     * @param prodProp
     * @return
     */
    @Override
    @CacheEvict(key = ProductConstants.ALL_PROD_PROP_LIST_KEY)
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyProdSpec(ProdProp prodProp) {
        if (!StringUtils.hasText(prodProp.getPropName())) {
            throw new BusinessException("属性名不能为空，请重新添加!!!");
        }
        if (prodPropMapper.updateById(prodProp) > 0) {
            // 需要先删除原来的再添加
            prodPropValueMapper.delete(
                    new LambdaQueryWrapper<ProdPropValue>()
                            .eq(ProdPropValue::getPropId, prodProp.getPropId())
            );
            return saveBatchProdPropValue(prodProp);
        }
        return false;
    }

    /**
     * 删除商品规格信息
     * @param propId
     * @return
     */
    @Override
    @CacheEvict(key = ProductConstants.ALL_PROD_PROP_LIST_KEY)
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeProdSpecById(Long propId) {
        prodPropValueMapper.delete(
                new LambdaQueryWrapper<ProdPropValue>()
                        .eq(ProdPropValue::getPropId, propId)
        );

        return prodPropMapper.deleteById(propId) > 0;
    }

    @Override
    @Cacheable(key = ProductConstants.ALL_PROD_PROP_LIST_KEY)
    public List<ProdProp> queryProdSpecList() {
        return prodPropMapper.selectList(null);
    }

    /**
     * 根据属性id查询对应的属性值
     * @param propId
     * @return
     */
    @Override
    public List<ProdPropValue> querySpecValueList(Long propId) {
        return prodPropValueMapper.selectList(
                new LambdaQueryWrapper<ProdPropValue>()
                        .eq(ProdPropValue::getPropId, propId)
                        .orderByDesc(ProdPropValue::getValueId)
        );
    }

    /**
     * 批量存储属性值
     * @param prodProp
     * @return
     */
    private Boolean saveBatchProdPropValue(ProdProp prodProp) {
        List<ProdPropValue> propValueList = prodProp.getProdPropValues();
        if (CollectionUtil.isNotEmpty(propValueList)) {
            Long propId = prodProp.getPropId();
            propValueList.forEach(prodPropValue -> prodPropValue.setPropId(propId));
            return prodPropValueService.saveBatch(propValueList);
        }
        return true;
    }
}
