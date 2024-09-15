package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.domain.ProdTagReference;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.feign.ProductClient;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 张健宁
 * @ClassName SearchServiceImpl
 * @Description TODO
 * @createTime 2024年09月15日 22:10:00
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ProductClient productClient;

    /**
     * 小程序：根据商品分组标签id分页查询产品
     * @param current
     * @param size
     * @param tagId
     * @return
     */
    @Override
    public Page<Prod> queryWxProdPageByTagId(Long current, Long size, Long tagId) {
        Page<Prod> prodPage = new Page<>();

        // 先根据分组标签查询到与其关联的商品id集合
        Result<Page<ProdTagReference>> resultProdTagReference = productClient.getProdTagReferencePageByTagId(current, size, tagId);
        if (resultProdTagReference.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口获取商品标签关系失败，请重试！");
        }
        Page<ProdTagReference> prodTagReferencePage = resultProdTagReference.getData(); // 查询后的商品标签关系分页对象
        List<ProdTagReference> prodTagReferenceList = prodTagReferencePage.getRecords(); // 商品标签关系集合
        if (CollectionUtil.isEmpty(prodTagReferenceList)) {
            return prodPage;
        }

        // 根据商品id集合分页查询商品信息
        // 商品id集合
        List<Long> prodIdList = prodTagReferenceList.stream().map(ProdTagReference::getProdId).collect(Collectors.toList());
        Result<List<Prod>> resultProd = productClient.loadProdInfoByIds(prodIdList);
        if (resultProd.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口获取商品信息失败，请重试！");
        }
        List<Prod> prodList = resultProd.getData();
        if (CollectionUtil.isEmpty(prodList)) {
            return prodPage;
        }
        prodPage.setRecords(prodList);
        prodPage.setTotal(prodTagReferencePage.getTotal());
        prodPage.setPages(prodTagReferencePage.getPages());

        return prodPage;
    }

    /**
     * 小程序：根据商品类目id查询商品集合
     * 1. 当前传入的只是商品一级类目
     * 2. 需要查询出该一级类目下所有子类目所对应的商品
     * @param categoryId
     * @return
     */
    @Override
    public List<Prod> queryWxProdListByCategoryId(Long categoryId) {
        ArrayList<Long> categoryList = new ArrayList<>();

        // 查询出当前categoryId所对应的子类目集合
        Result<List<Long>> resultCategory = productClient.getCategoryChildIdsByCategoryId(categoryId);
        if (resultCategory.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口：获取类目的子目录id集合失败，请重试！");
        }
        List<Long> childrenId = resultCategory.getData();
        if (CollectionUtil.isNotEmpty(childrenId)) {
            categoryList.addAll(childrenId);
        }
        categoryList.add(categoryId);

        // 根据所有的类目id查询出对应的商品信息
        Result<List<Prod>> resultProd = productClient.getProdListByCategoryIds(categoryList);
        if (resultProd.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("Feign接口：获取类目的子目录id集合失败，请重试！");
        }
        return resultProd.getData();
    }
}
