package com.zjn.mall.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.MemberCollectionMapper;
import com.zjn.mall.domain.MemberCollection;
import com.zjn.mall.service.MemberCollectionService;
/**
 * @ClassName MemberCollectionServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月12日 12:40:00
 */

@Service
public class MemberCollectionServiceImpl extends ServiceImpl<MemberCollectionMapper, MemberCollection> implements MemberCollectionService{

}
