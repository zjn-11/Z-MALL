package com.zjn.mall.service.impl;

import com.zjn.mall.constants.StoreConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.domain.Area;
import com.zjn.mall.mapper.AreaMapper;
import com.zjn.mall.service.AreaService;
import sun.java2d.marlin.ArrayCacheConst;

/**
 * @ClassName AreaServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月11日 16:58:00
 */

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "com.zjn.mall.service.impl.AreaServiceImpl")
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService{
    private final AreaMapper areaMapper;

    /**
     * 查询全国地区信息列表
     * @return
     */
    @Override
    @Cacheable(key = StoreConstants.ALL_AREA_LIST_KEY)
    public List<Area> queryALLAreaList() {
        List<Area> areas = areaMapper.selectList(null);
        return areas;
    }
}
