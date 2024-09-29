package com.zjn.mall.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjn.mall.constant.ResourceConstants;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.constants.HttpConstants;
import com.zjn.mall.filter.TokenTranslationFilter;
import com.zjn.mall.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 张健宁
 * @ClassName ResourceServerConfig
 * @Description SpringSecurity资源服务器配置类
 * @createTime 2024年09月04日 14:04:00
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Order(100)
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    private final TokenTranslationFilter tokenTranslationFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭跨站请求伪造
        http.cors().disable();
        // 关闭跨域请求
        http.csrf().disable();
        // 关闭session使用策略，即不需要放置与session中
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 在security验证之前解析token，将用户信息放入当前的资源服务器中
        http.addFilterBefore(tokenTranslationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint()) // 处理没有携带token的请求
                .accessDeniedHandler(accessDeniedHandler()); // 处理有token但权限不足的请求

        http.authorizeHttpRequests()
                .antMatchers(ResourceConstants.RESOURCE_ALLOW_URLS).permitAll() // 匹配的请求不需要验证
                .anyRequest().authenticated(); // 为被匹配的所有请求都需要验证

    }

    /**
     * 处理无token的请求
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                Result<Object> fail = Result.fail(BusinessEnum.UN_AUTHORIZATION);
                returnResult(response, fail);
            }
        };
    }

    /**
     * 处理有token但权限不足的请求
     * @return
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                Result<Object> fail = Result.fail(BusinessEnum.access_deny_fail);
                returnResult(response, fail);
            }
        };
    }

    /**
     * 公共的返回结果Result函数
     * @param response
     * @param result
     * @throws IOException
     */
    public void returnResult(HttpServletResponse response, Result<?> result) throws IOException {
        // 设置响应信息
        response.setContentType(HttpConstants.APPLICATION_JSON);
        response.setCharacterEncoding(HttpConstants.UTF_8);
        // 返回统一结果
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(result);
        PrintWriter writer = response.getWriter();
        writer.write(s);
        writer.flush();
        writer.close();
    }
}
