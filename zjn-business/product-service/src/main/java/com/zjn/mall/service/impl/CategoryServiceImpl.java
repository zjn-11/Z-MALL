package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.constants.ProductConstants;
import com.zjn.mall.ex.handler.BusinessException;
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
    public List<Category> queryFirstCategoryList() {
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

    /**
     * 修改商品类目信息
     * 因为一级变二级要求一级之下不能有二级，所以需要先从数据库查出原来的parentId
     * @param category
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(key = ProductConstants.ALL_CATEGORY_LIST_KEY),
            @CacheEvict(key = ProductConstants.FIRST_CATEGORY_LIST_KEY)
    })
    public Boolean modifyCategory(Category category) {
        // 根据id查询类目详情
        Category categoryBefore = categoryMapper.selectById(category.getCategoryId());
        Long beforeParentId = categoryBefore.getParentId();
        Long newParentId = category.getParentId();
        // 判断商品类目的修改情况
        // 1 -> 1: beforeParentId = 0 && newParentId = 0 无需特殊处理
        // 2 -> 2: beforeParentId != 0 && newParentId != 0 无需特殊处理
        if (beforeParentId.equals(0L)) {
            if (ObjectUtil.isNotNull(newParentId) && !newParentId.equals(0L)) {
                // 1 -> 2: beforeParentId = 0 && newParentId != 0
                // 查看当前类目是否有子类目，如果有，则不允许修改
                List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId, category.getCategoryId()));
                if (CollectionUtil.isNotEmpty(categories)) {
                    throw new BusinessException("一级类目具有子目录，不允许修改为二级目录！");
                }
            }
        } else if (!beforeParentId.equals(0L) && ObjectUtil.isNull(newParentId)) {
            // 2 -> 1: beforeParentId != 0 && newParentId = null 因为新的parentId为空，所以需要设置为0
            category.setParentId(0L);
        }
        return categoryMapper.updateById(category) > 0;
    }
}
