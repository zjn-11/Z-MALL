package com.zjn.mall.ex.handler;

import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * @author 张健宁
 * @ClassName GlobalExceptionHandler
 * @Description 异常处理器
 * @createTime 2024年09月04日 13:44:00
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public Result<String> businessException(BusinessException e) {
        log.error(e.getMessage());
        return Result.fail(BusinessEnum.OPERATION_FAIL.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Result<String> runtimeException(RuntimeException e) {
        log.error(e.getMessage());
        return Result.fail(BusinessEnum.SERVER_INNER_ERROR);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public Result<String> AccessDeniedException(AccessDeniedException e) throws AccessDeniedException {
        log.error(e.getMessage());
        throw e;
    }


}
