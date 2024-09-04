package com.zjn.mall.config;

import cn.hutool.core.util.ObjectUtil;
import com.zjn.mall.constants.AuthConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 张健宁
 * @ClassName FeignInterceptor
 * @Description feign调用间的token传递
 * @createTime 2024年09月04日 15:22:00
 */
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNotNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (ObjectUtil.isNotNull(request)) {
                String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
                requestTemplate.header(AuthConstants.AUTHORIZATION, authorization);
                return;
            }
        }

        // 从定时器过来的请求是没有token的
        // TODO 需要给这里设置一个永不过期的token
        requestTemplate.header(AuthConstants.AUTHORIZATION, AuthConstants.BEARER +  ""/* TODO token */);
    }
}
