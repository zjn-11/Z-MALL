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

    /**
     * redis中所有状态正常的商品分组标签的key
     */
    String PROD_TAG_NORMAL_LIST_KEY = "'normalProdTag'";

    /**
     * redis中所有商品属性的key
     */
    String ALL_PROD_PROP_LIST_KEY = "'allProdProp'";
}
