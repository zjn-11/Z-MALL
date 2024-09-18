package com.zjn.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjn.mall.dto.ProdCommonViewDto;

/**
 * @ClassName ProdCommService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

public interface ProdCommService extends IService<ProdComm>{

    Boolean modifyProdComm(ProdComm prodComm);

    ProdCommonViewDto ProdCommonViewByProdId(Long prodId);

    Page<ProdComm> queryWxProdCommPageByProd(Long prodId, Long size, Long current, Integer evaluate);
}
