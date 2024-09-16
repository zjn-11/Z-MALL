package com.zjn.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.dto.ProdCommonViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
}
