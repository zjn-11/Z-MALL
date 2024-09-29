package com.zjn.mall.filter;

import cn.hutool.json.JSONUtil;
import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.model.SecurityUser;
import com.zjn.mall.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 张健宁
 * @ClassName TokenTranslationFilter
 * @Description token解析过滤器，将token转换为security可以认证的用户对象
 * @createTime 2024年09月04日 14:28:00
 */

@Component
@RequiredArgsConstructor
public class TokenTranslationFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;

    /**
     * 网关到资源服务器的token传递：
     * 1. 只转换携带token的请求
     * 2. 转换得到用户信息
     * 3. 没有携带token的请求交给ResourceServerConfig的handler处理
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    // 使用JWT的验证方法
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
        if (StringUtils.hasText(authorization)) {
            String token = authorization.replaceFirst(AuthConstants.BEARER, "");
            // 解析token
            if (JwtUtils.verify(token)) {
                // token正确
                Object username = JwtUtils.getInfo(token);
                // 拼接统一前缀和用户名得到redisKey
                String redisKey = AuthConstants.LOGIN_TOKEN_PREFIX + username;
                if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
                    // 如果redis存在
                    // token续签问题,如果小于阈值，就对其续签
                    Long expire = redisTemplate.getExpire(redisKey);
                    if (expire != null && expire < AuthConstants.TOKEN_EXPIRE_THRESHOLD_TIME) {
                        redisTemplate.expire(redisKey, AuthConstants.TOKEN_TIME, TimeUnit.SECONDS);
                    }
                    // 从redis取出json并解析为security可认证的用户对象，将认证用户对象放入context中
                    String userJson = redisTemplate.opsForValue().get(redisKey);
                    SecurityUser securityUser = JSONUtil.toBean(userJson, SecurityUser.class);
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                            securityUser, null,
                            securityUser.getPerms().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
                    ));
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    /* 使用UUID时的验证方法
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
        if (StringUtils.hasText(authorization)) {
            String token = authorization.replaceFirst(AuthConstants.BEARER, "");
            if (StringUtils.hasText(authorization)) {
                token = AuthConstants.LOGIN_TOKEN_PREFIX + token;
                if (Boolean.TRUE.equals(redisTemplate.hasKey(token))) {
                    // token续签问题,如果小于阈值，就对其续签
                    Long expire = redisTemplate.getExpire(token);
                    if (expire < AuthConstants.TOKEN_EXPIRE_THRESHOLD_TIME) {
                        redisTemplate.expire(token, AuthConstants.TOKEN_TIME, TimeUnit.SECONDS);
                    }
                    // 从redis取出json并解析为security可认证的用户对象，将认证用户对象放入context中
                    String userJson = redisTemplate.opsForValue().get(token);
                    SecurityUser securityUser = JSONUtil.toBean(userJson, SecurityUser.class);
                    SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                            securityUser, null,
                            securityUser.getPerms().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
                        )
                    );
                }
            }
        }
        filterChain.doFilter(request, response);
    }*/
}
