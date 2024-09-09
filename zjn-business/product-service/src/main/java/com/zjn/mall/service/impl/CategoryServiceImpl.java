package com.zjn.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.ProductConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.CategoryMapper;
import com.zjn.mall.domain.Category;
import com.zjn.mall.service.CategoryService;
/**
 * @ClassName CategoryServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "com.zjn.mall.impl.CategoryServiceImpl")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    private final CategoryMapper categoryMapper;

    /**
     * 查询所有的商品类目
     * @return
     */
    @Override
    @Cacheable(key = ProductConstants.ALL_CATEGORY_LIST_KEY)
    public List<Category> queryAllCategoryList() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .orderByDesc(Category::getSeq)
        );
    }

    /**
     * 查询所有的一级类目
     * @return
     */
    @Override
    @Cacheable(key = ProductConstants.FIRST_CATEGORY_LIST_KEY)
    public List<Category> quaryFirstCategoryList() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getParentId, 0)
                        .eq(Category::getStatus, 1)
                        .orderByDesc(Category::getSeq)
        );
    }

    /**
     * 新增商品一级类目
     * @param category
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(key = ProductConstants.ALL_CATEGORY_LIST_KEY),
            @CacheEvict(key = ProductConstants.FIRST_CATEGORY_LIST_KEY)
    })
    public Boolean saveCategory(Category category) {
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        return categoryMapper.insert(category) > 0;
    }
}
