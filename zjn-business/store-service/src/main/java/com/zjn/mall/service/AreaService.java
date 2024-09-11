package com.zjn.mall.service;

import com.zjn.mall.domain.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName AreaService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月11日 16:58:00
 */

public interface AreaService extends IService<Area>{

    /**
     * 查询全国地区信息列表
     * @return
     */
    List<Area> queryALLAreaList();
}
