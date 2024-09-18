package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.domain.Member;
import com.zjn.mall.dto.ProdCommonViewDto;
import com.zjn.mall.ex.handler.BusinessException;
import com.zjn.mall.feign.MemberClient;
import com.zjn.mall.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.ProdCommMapper;
import com.zjn.mall.domain.ProdComm;
import com.zjn.mall.service.ProdCommService;
import org.springframework.util.StringUtils;

/**
 * @ClassName ProdCommServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

@Service
@RequiredArgsConstructor
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService{

    private final ProdCommMapper prodCommMapper;

    private final MemberClient memberClient;

    /**
     * 编辑：恢复和审核评论
     * @param prodComm
     * @return
     */
    @Override
    public Boolean modifyProdComm(ProdComm prodComm) {
        String replyContent = prodComm.getReplyContent();
        if (StringUtils.hasText(replyContent)) {
            // 如果有评论，reply_sts设置为一（已回复），设置恢复时间
            prodComm.setReplySts(1);
            prodComm.setReplyTime(new Date());
        }
        return prodCommMapper.updateById(prodComm) > 0;
    }

    /**
     * 小程序：根据商品Id返回商品评论总览数据
     * 0好评 1中评 2差评
     * 总评论数
     * 有图的评论数量
     * 好评率
     * @param prodId
     * @return
     */
    @Override
    public ProdCommonViewDto ProdCommonViewByProdId(Long prodId) {
        ProdCommonViewDto prodCommonViewDto = new ProdCommonViewDto();

        List<ProdComm> prodCommList = prodCommMapper.selectList(
                new LambdaQueryWrapper<ProdComm>()
                        .eq(ProdComm::getProdId, prodId)
                        .eq(ProdComm::getStatus, 1)
        );
        if (CollectionUtil.isEmpty(prodCommList)) {
            return prodCommonViewDto;
        }

        AtomicInteger good = new AtomicInteger();
        AtomicInteger second = new AtomicInteger();
        AtomicInteger bad = new AtomicInteger();
        AtomicInteger picCount = new AtomicInteger();
        prodCommList.forEach(prodComm -> {
            if (prodComm.getEvaluate().equals(0)) {
                good.getAndIncrement();
            } else if (prodComm.getEvaluate().equals(1)) {
                second.getAndIncrement();
            } else if (prodComm.getEvaluate().equals(2)) {
                bad.getAndIncrement();
            }
            if (StringUtils.hasText(prodComm.getPics())) {
                picCount.getAndIncrement();
            }
        });

        prodCommonViewDto.setAllCount(prodCommList.size());
        prodCommonViewDto.setGoodCount(good.get());
        prodCommonViewDto.setSecondCount(second.get());
        prodCommonViewDto.setBadCount(bad.get());
        prodCommonViewDto.setPicCount(picCount.get());
        if (prodCommList.size() != 0) {
            prodCommonViewDto.setGoodLv(
                    new BigDecimal(good.get())
                            .divide(new BigDecimal(prodCommList.size()),
                            3, RoundingMode.HALF_DOWN)
                            .multiply(new BigDecimal(100))
            );
        } else {
            prodCommonViewDto.setGoodLv(BigDecimal.ZERO);
        }
        return prodCommonViewDto;
    }

    /**
     * 小程序：多条件分页查询评论信息
     * @param prodId
     * @param size
     * @param current
     * @param evaluate
     * @return
     */
    @Override
    public Page<ProdComm> queryWxProdCommPageByProd(Long prodId, Long size, Long current, Integer evaluate) {
        Page<ProdComm> prodCommPage = new Page<>(current, size);
        prodCommPage = prodCommMapper.selectPage(prodCommPage,
                new LambdaQueryWrapper<ProdComm>()
                        .eq(ProdComm::getProdId, prodId)
                        .eq(ProdComm::getStatus, 1)
                        .eq(evaluate.equals(0) || evaluate.equals(1) || evaluate.equals(2), ProdComm::getEvaluate, evaluate)
                        .isNotNull(evaluate.equals(3), ProdComm::getPics)
                        .orderByDesc(ProdComm::getScore, ProdComm::getCreateTime)
        );

        List<ProdComm> prodCommList = prodCommPage.getRecords();
        if (CollectionUtil.isEmpty(prodCommList)) {
            return prodCommPage;
        }

        // 查询所有用户的openid，获取会员对象集合
        List<String> openidList = prodCommList.stream().map(ProdComm::getOpenId).collect(Collectors.toList());
        Result<List<Member>> result = memberClient.getMembersByOpenidList(openidList);
        if (result.getCode().equals(BusinessEnum.OPERATION_FAIL.getCode())) {
            throw new BusinessException("远程接口：通过openidList获取会员集合失败，请重试！！！");
        }
        List<Member> memberList = result.getData();
        if (CollectionUtil.isEmpty(memberList)) {
            return prodCommPage;
        }
        prodCommList.forEach(prodComm -> {
            Member m = memberList.stream()
                    .filter(member -> member.getOpenId().equals(prodComm.getOpenId()))
                    .collect(Collectors.toList()).get(0);
            // 会员名称脱敏操作
            StringBuilder stringBuilder = new StringBuilder(m.getNickName());
            StringBuilder replace = stringBuilder.replace(1, stringBuilder.length() - 1, "***");
            prodComm.setNickName(replace.toString());
            prodComm.setPic(m.getPic());
        });

        return prodCommPage;
    }
}
