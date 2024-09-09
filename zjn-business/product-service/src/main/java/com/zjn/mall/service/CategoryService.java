package com.zjn.mall.service;

import com.zjn.mall.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName CategoryService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

public interface CategoryService extends IService<Category>{

    List<Category> queryAllCategoryList();

    List<Category> quaryFirstCategoryList();

    Boolean saveCategory(Category category);
}
