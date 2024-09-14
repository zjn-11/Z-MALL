package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.domain.Prod;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.feign.ProductClient;
import com.zjn.mall.model.Result;
import com.zjn.mall.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.MemberCollectionMapper;
import com.zjn.mall.domain.MemberCollection;
import com.zjn.mall.service.MemberCollectionService;
/**
 * @ClassName MemberCollectionServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 12:40:00
 */

@Service
@RequiredArgsConstructor
public class MemberCollectionServiceImpl extends ServiceImpl<MemberCollectionMapper, MemberCollection> implements MemberCollectionService{

    private final MemberCollectionMapper memberCollectionMapper;
    private final ProductClient productClient;

    /**
     * 查询用户收藏商品详情
     * @param prodPage
     * @return
     */
    @Override
    public Page<Prod> queryMemberCollectionPage(Page<Prod> prodPage) {
        String openid = AuthUtils.getLoginMemberOpenid();
        Page<MemberCollection> memberCollectionPage = new Page<>();
        // 先查出收藏商品的id
        memberCollectionPage = memberCollectionMapper.selectPage(
                memberCollectionPage,
                new LambdaQueryWrapper<MemberCollection>()
                        .eq(MemberCollection::getOpenId, openid)
        );
        List<MemberCollection> memberCollectionList = memberCollectionPage.getRecords();
        if (CollectionUtil.isEmpty(memberCollectionList)) {
            return prodPage;
        }

        List<Long> prodIds = memberCollectionList.stream().map(MemberCollection::getProdId).collect(Collectors.toList());

        Result<List<Prod>> result = productClient.loadProdInfoByIds(prodIds);
        if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("调用商品服务根据idList查询商品服务失败：请重试！");
        }

        List<Prod> prodList = result.getData();
        prodPage.setRecords(prodList);
        prodPage.setTotal(memberCollectionPage.getTotal());
        prodPage.setPages(memberCollectionPage.getPages());

        return prodPage;
    }
}
