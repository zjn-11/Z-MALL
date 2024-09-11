package com.zjn.mall.service;

import com.zjn.mall.domain.Prod;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * @ClassName ProdService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

public interface ProdService extends IService<Prod>{

    Boolean saveProd(Prod prod);

    Prod queryProdInfoById(Long id);

    Boolean modifyProdInfo(Prod prod);

    Boolean removeProdById(Long id);
}
