package com.zjn.mall.service;

import com.zjn.mall.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * @ClassName ProdCommService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

public interface ProdCommService extends IService<ProdComm>{

    Boolean modifyProdComm(ProdComm prodComm);

}
