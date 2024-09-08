package com.zjn.mall.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author 张健宁
 * @ClassName SysLogAspect
 * @Description 系统操作日志切面类
 * @createTime 2024年09月08日 21:05:00
 */

@Component
@Aspect
@Slf4j
public class SysLogAspect {
    /**
     * 切点表达式
     */
    public static final String POINT_CUT = "execution(* com.zjn.mall.controller.*.*(..))";

    /**
     *
     * @return
     */
    @Around(value = POINT_CUT)
    public Object logAround(ProceedingJoinPoint joinPoint) {
        Object result = null;
        // 获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取ip地址
        String host = request.getRemoteHost();
        // 获取请求路径
        String path = request.getRequestURI();
        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        // 获取请求方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.toString();
        // 获取描述信息->方法上的ApiOperation注解
        ApiOperation apiOperation = method.getDeclaredAnnotation(ApiOperation.class);
        String operation = null;
        if (ObjectUtil.isNotNull(apiOperation)) {
            operation = apiOperation.value();
        }
        // 判断参数类型
        String finalArgs = "";
        if (ObjectUtil.isNotEmpty(args) && args[0] instanceof MultipartFile) {
            // 如果是文件类型，就不转为json
            finalArgs = "file";
        } else {
            // 如果不是文件类型，就转为JSON格式的字符串
            finalArgs = JSONUtil.toJsonStr(apiOperation);
        }
        // 记录方法执行时间
        long start = System.currentTimeMillis();
        try {
            // 执行方法
            result = joinPoint.proceed(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        // 方法执行时长
        long execTime = end - start;

        log.info("调用时间：{}，\n请求接口路径：{}，\n请求IP地址：{}，\n方法名称：{}，\n执行时长：{}，\n方法描述：{}，\n，请求参数：{}\n",
                new Date(),
                path,
                host,
                methodName,
                execTime,
                operation,
                finalArgs
                );

        return result;
    }

}
