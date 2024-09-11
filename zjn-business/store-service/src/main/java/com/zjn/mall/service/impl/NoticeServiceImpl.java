package com.zjn.mall.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjn.mall.mapper.NoticeMapper;
import com.zjn.mall.domain.Notice;
import com.zjn.mall.service.NoticeService;
/**
 * @ClassName NoticeServiceImpl
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月11日 16:58:00
 */

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{

}
