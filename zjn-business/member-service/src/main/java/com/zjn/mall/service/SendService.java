package com.zjn.mall.service;

import java.util.Map;

/**
 * @author 张健宁
 * @ClassName SendService
 * @Description TODO
 * @createTime 2024年09月13日 17:07:00
 */
public interface SendService {
    void sendPhoneMsg(Map<String, Object> map) throws Exception;

    Boolean saveMsgPhone(Map<String, Object> map);
}
