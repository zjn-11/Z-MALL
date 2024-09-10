package com.zjn.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.ProdProp;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * @ClassName ProdPropService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

public interface ProdPropService extends IService<ProdProp>{

    Page<ProdProp> queryProdSpecPage(Long current, Long size, String propName);

    Boolean saveProdSpec(ProdProp prodProp);

    Boolean modifyProdSpec(ProdProp prodProp);

    Boolean removeProdSpecById(Long propId);
}
