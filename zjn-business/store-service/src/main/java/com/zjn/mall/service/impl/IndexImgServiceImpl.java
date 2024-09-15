package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.constants.StoreConstants;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.feign.ProductClient;
import com.zjn.mall.model.Result;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.IndexImg;
import com.zjn.mall.mapper.IndexImgMapper;
import com.zjn.mall.service.IndexImgService;
/**
 * @ClassName IndexImgServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月11日 16:58:00
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "com.zjn.mall.service.impl.IndexImgServiceImpl")
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg> implements IndexImgService{

    private final IndexImgMapper indexImgMapper;

    private final ProductClient productClient;

    /**
     * 新增轮播图信息
     * @param indexImg
     * @return
     */
    @Override
    @CacheEvict(key = StoreConstants.WX_INDEX_IMG_KEY)
    public Boolean saveIndexImg(IndexImg indexImg) {
        indexImg.setCreateTime(new Date());
        indexImg.setShopId(1L);
        // 判断关联关系
        if (indexImg.getType().equals(-1)) {
            // 不关联商品
            indexImg.setProdId(null);
        }
        return indexImgMapper.insert(indexImg) > 0;
    }

    /**
     * 根据id查询轮播图信息
     * 同时如果type=0就需要查出对应的商品信息
     * @param imgId
     * @return
     */
    @Override
    public IndexImg queryIndexImgById(Long imgId) {
        IndexImg indexImg = indexImgMapper.selectById(imgId);
        Integer type = indexImg.getType();
        if (type.equals(0)) {
            // 需要获取商品信息
            Long prodId = indexImg.getProdId();
            Result<List<Prod>> result = productClient.loadProdInfoByIds(Collections.singletonList(prodId));
            // 判断是否正确
            if (BusinessEnum.OPERATION_FAIL.getCode().equals(result.getCode())) {
                throw new BusinessException(result.getMsg());
            }
            List<Prod> prodList = result.getData();
            if (CollectionUtil.isNotEmpty(prodList)) {
                // 获取商品对象
                Prod prod = prodList.get(0);
                indexImg.setPic(prod.getPic());
                indexImg.setProdName(prod.getProdName());
            }
        }
        return indexImg;
    }

    /**
     * 修改轮播图信息
     * @param indexImg
     * @return
     */
    @Override
    @CacheEvict(key = StoreConstants.WX_INDEX_IMG_KEY)
    public Boolean modifyIndexImg(IndexImg indexImg) {
        return indexImgMapper.updateById(indexImg) > 0;
    }

    /**
     * 删除轮播图信息
     * @param imgIds
     * @return
     */
    @Override
    @CacheEvict(key = StoreConstants.WX_INDEX_IMG_KEY)
    public Boolean removeIndexImgByIds(List<Long> imgIds) {
        return indexImgMapper.deleteBatchIds(imgIds) == imgIds.size();
    }

    /**
     * 小程序首页获取轮播图
     * 查询出状态正常 status=1 的轮播图
     * 并存入redis中
     * @return
     */
    @Override
    @Cacheable(key = StoreConstants.WX_INDEX_IMG_KEY)
    public List<IndexImg> queryWxIndexImgList() {
        return indexImgMapper.selectList(
                new LambdaQueryWrapper<IndexImg>()
                        .eq(IndexImg::getStatus, 1)
                        .orderByDesc(IndexImg::getSeq)
        );
    }
}
