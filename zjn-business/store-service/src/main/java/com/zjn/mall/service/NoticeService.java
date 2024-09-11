package com.zjn.mall.service;

import com.zjn.mall.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * @ClassName NoticeService
 * @author 张健宁
 * @Description TODO
 * @createTime 2024年09月11日 16:58:00
 */

public interface NoticeService extends IService<Notice>{


    Boolean saveShopNotice(Notice notice);

    Boolean modifyShopNotice(Notice notice);
}
