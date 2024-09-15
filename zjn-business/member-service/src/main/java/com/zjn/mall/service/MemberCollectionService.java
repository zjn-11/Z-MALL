package com.zjn.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.MemberCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjn.mall.domain.Prod;

/**
 * @ClassName MemberCollectionService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 12:40:00
 */

public interface MemberCollectionService extends IService<MemberCollection>{

    Page<Prod> queryMemberCollectionPage(Page<Prod> prodPage);

    Boolean checkIsCollectionByProdId(Long prodId, String openid);

    Boolean addOrCancelMemberCollection(Long prodId, String openid);
}
