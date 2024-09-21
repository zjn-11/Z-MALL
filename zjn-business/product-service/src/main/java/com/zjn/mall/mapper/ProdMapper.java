package com.zjn.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjn.mall.domain.Prod;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @ClassName ProdMapper
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月09日 22:23:00
 */

public interface ProdMapper extends BaseMapper<Prod> {
    @Update("update prod set total_stocks = total_stocks + #{count}, " +
            "sold_num = sold_num - #{count}, version = version + 1 " +
            "where prod_id = #{prodId} " +
            "and version = #{version} " +
            "and (total_stocks + #{count}) >= 0"
    )
    Integer updateProdStock(@Param("prodId") Long prodId, @Param("count") Integer count, @Param("version") Integer version);
}