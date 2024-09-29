package com.zjn.mall.filter;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjn.mall.config.AuthProperties;
import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.constants.HttpConstants;
import com.zjn.mall.model.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * @author 张健宁
 * @ClassName AuthFilter
 * @Description 全局token过滤器，统一放在Authorization bearer token
 * @createTime 2024年09月03日 17:12:00
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher;
    private final StringRedisTemplate redisTemplate;

    /**
     * 对请求路径进行判断，并进行验证
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        if (isExclude(path))
            return chain.filter(exchange);

        String token = null;
        String authorization = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION);
        if (StringUtils.hasText(authorization)) {
            // 只判断Authorization是否有值，具体的校验放到oncePerRequest中完成
            token = authorization.replaceFirst(AuthConstants.BEARER, "");
            if (StrUtil.isNotBlank(token)) {
                return chain.filter(exchange);
            }
        }

        log.error("拦截非法请求，时间{},请求API路径：{}", new Date(), path);

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON);

        Result<Object> result = Result.fail(BusinessEnum.UN_AUTHORIZATION);
        byte[] bytes = new byte[0];
        try {
            bytes = new ObjectMapper().writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(dataBuffer));
    }

    private boolean isExclude(String path) {
        for (String excludePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(excludePath, path))
                return true;
        }
        return false;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
