package com.zjn.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.NoticeMapper;
import com.zjn.mall.domain.Notice;
import com.zjn.mall.service.NoticeService;

/**
 * @author 张健宁
 * @ClassName NoticeServiceImpl
 * @Description TODO
 * @createTime 2024年09月11日 16:58:00
 */

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public Boolean saveShopNotice(Notice notice) {
        notice.setShopId(1L);
        notice.setCreateTime(new Date());
        notice.setUpdateTime(new Date());
        return noticeMapper.insert(notice) > 0;
    }

    @Override
    public Boolean modifyShopNotice(Notice notice) {
        notice.setUpdateTime(new Date());
        return noticeMapper.updateById(notice) > 0;
    }
}
