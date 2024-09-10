package com.zjn.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.ProductConstants;
import com.zjn.mall.mapper.ProdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.ProdTagMapper;
import com.zjn.mall.domain.ProdTag;
import com.zjn.mall.service.ProdTagService;
/**
 * @ClassName ProdTagServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "com.zjn.mall.service.impl.ProdTagServiceImpl")
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService{

    private final ProdTagMapper prodTagMapper;

    /**
     * 新增商品分组标签
     * @param prodTag
     * @return
     */
    @Override
    @CacheEvict(key = ProductConstants.PROD_TAG_NORMAL_LIST_KEY)
    public Boolean saveProdTag(ProdTag prodTag) {
        prodTag.setCreateTime(new Date());
        prodTag.setUpdateTime(new Date());
        return prodTagMapper.insert(prodTag) > 0;
    }

    /**
     * 修改商品分组标签
     * @param prodTag
     * @return
     */
    @Override
    @CacheEvict(key = ProductConstants.PROD_TAG_NORMAL_LIST_KEY)
    public Boolean modifyProdTag(ProdTag prodTag) {
        prodTag.setUpdateTime(new Date());
        return prodTagMapper.updateById(prodTag) > 0;
    }

    /**
     * 删除商品分组标签
     * @param id
     * @return
     */
    @Override
    @CacheEvict(key = ProductConstants.PROD_TAG_NORMAL_LIST_KEY)
    public Boolean removeProdTagById(Long id) {
        return super.removeById(id);
    }

    /**
     * 查询状态正常的分组标签集合
     * @return
     */
    @Override
    @Cacheable(key = ProductConstants.PROD_TAG_NORMAL_LIST_KEY)
    public List<ProdTag> loadProdTagList() {
        return prodTagMapper.selectList(
                new LambdaQueryWrapper<ProdTag>()
                        .eq(ProdTag::getStatus, 1)
                        .orderByDesc(ProdTag::getSeq)
        );
    }
}
