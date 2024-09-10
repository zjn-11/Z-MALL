package com.zjn.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
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
}
