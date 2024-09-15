package com.zjn.mall.service;

import com.zjn.mall.domain.IndexImg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName IndexImgService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月11日 16:58:00
 */

public interface IndexImgService extends IService<IndexImg>{

    Boolean saveIndexImg(IndexImg indexImg);

    IndexImg queryIndexImgById(Long imgId);

    Boolean modifyIndexImg(IndexImg indexImg);

    List<IndexImg> queryWxIndexImgList();

    Boolean removeIndexImgByIds(List<Long> imgIds);
}
