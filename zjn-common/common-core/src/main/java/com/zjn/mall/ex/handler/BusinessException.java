package com.zjn.mall.ex.handler;

/**
 * @author 张健宁
 * @ClassName BusinessException
 * @Description TODO
 * @createTime 2024年09月04日 13:59:00
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
