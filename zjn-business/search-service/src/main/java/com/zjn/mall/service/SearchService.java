package com.zjn.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.Prod;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName SearchService
 * @Description TODO
 * @createTime 2024年09月15日 22:09:00
 */
public interface SearchService {
    Page<Prod> queryWxProdPageByTagId(Long current, Long size, Long tagId);

    List<Prod> queryWxProdListByCategoryId(Long categoryId);
}
