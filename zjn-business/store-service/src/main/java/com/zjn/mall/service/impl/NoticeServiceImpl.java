package com.zjn.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.constants.StoreConstants;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.checkerframework.checker.units.qual.C;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
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
@CacheConfig(cacheNames = "com.zjn.mall.service.impl.NoticeServiceImpl")
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    private final NoticeMapper noticeMapper;


    /**
     * 保存公告信息
     * @param notice
     * @return
     */
    @Override
    @CacheEvict(key = StoreConstants.WX_TOP_NOTICE_LIST_KEY)
    public Boolean saveShopNotice(Notice notice) {
        notice.setShopId(1L);
        notice.setCreateTime(new Date());
        notice.setUpdateTime(new Date());
        return noticeMapper.insert(notice) > 0;
    }

    /**
     * 修改公告信息
     * @param notice
     * @return
     */
    @Override
    @CacheEvict(key = StoreConstants.WX_TOP_NOTICE_LIST_KEY)
    public Boolean modifyShopNotice(Notice notice) {
        notice.setUpdateTime(new Date());
        return noticeMapper.updateById(notice) > 0;
    }

    /**
     * 删除公告信息
     * @param id
     * @return
     */
    @Override
    @CacheEvict(key = StoreConstants.WX_TOP_NOTICE_LIST_KEY)
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 小程序查询置顶公告消息
     * @return
     */
    @Override
    @Cacheable(key = StoreConstants.WX_TOP_NOTICE_LIST_KEY)
    public List<Notice> queryWxTopNoticeList() {
        return noticeMapper.selectList(
                new LambdaQueryWrapper<Notice>()
                        .eq(Notice::getStatus, 1)
                        .eq(Notice::getIsTop, 1)
                        .orderByDesc(Notice::getUpdateTime)

        );
    }
}
