package com.zjn.mall.service.impl;

import com.zjn.mall.mapper.ProdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.ProdTagMapper;
import com.zjn.mall.domain.ProdTag;
import com.zjn.mall.service.ProdTagService;
/**
 * @ClassName ProdTagServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

@Service
@RequiredArgsConstructor
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService{

    private final ProdTagMapper prodTagMapper;

    /**
     * 新增商品分组标签
     * @param prodTag
     * @return
     */
    @Override
    public Boolean saveProdTag(ProdTag prodTag) {
        prodTag.setCreateTime(new Date());
        prodTag.setUpdateTime(new Date());
        return prodTagMapper.insert(prodTag) > 0;
    }

    /**
     * 修改商品分组标签
     * @param prodTag
     * @return
     */
    @Override
    public Boolean modifyProdTag(ProdTag prodTag) {
        prodTag.setUpdateTime(new Date());
        return prodTagMapper.updateById(prodTag) > 0;
    }
}
