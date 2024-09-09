package com.zjn.mall.constants;

/**
 * @author 张健宁
 * @ClassName ProductConstants
 * @Description 产品管理模块常量类
 * @createTime 2024年09月09日 23:25:00
 */
public interface ProductConstants {

    /**
     * redis中商品所有类目的key
     */
    String ALL_CATEGORY_LIST_KEY = "'allCategory'";

    /**
     * redis中商品一级类目的key
     */
    String FIRST_CATEGORY_LIST_KEY = "'firstCategory'";
}
