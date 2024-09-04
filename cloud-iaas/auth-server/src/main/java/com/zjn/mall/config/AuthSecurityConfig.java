package com.zjn.mall.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.constants.BusinessEnum;
import com.zjn.mall.constants.HttpConstants;
import com.zjn.mall.impl.UserDetailServiceImpl;
import com.zjn.mall.model.LoginResult;
import com.zjn.mall.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.UUID;


/**
 * @author 张健宁
 * @ClassName AuthSecurityConfig
 * @Description Security安全框架配置类
 * @createTime 2024年09月03日 19:00:00
 */

@Configuration
@RequiredArgsConstructor
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailsService;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 确保Security使用自定义流程
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭跨站请求伪造
        http.cors().disable();
        // 关闭跨域请求
        http.csrf().disable();
        // 关闭session使用策略，即不需要放置与session中
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 配置登录信息
        http.formLogin()
                .loginProcessingUrl(AuthConstants.LOGIN_URL)
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler());
        // 配置登出信息
        http.logout()
                .logoutUrl(AuthConstants.LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler());

        // 要求所有请求都需要身份验证
        http.authorizeHttpRequests().anyRequest().authenticated();
    }

    /**
     * 登录成功处理器
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 使用UUID来当作token
            String token = UUID.randomUUID().toString();
            // 从security中获取认证用户对象并转换为json格式字符串
            String usrJsonStr = JSONUtil.toJsonStr(authentication.getPrincipal());
            // 将token当作key，用户认证对象的json字符串作为value存入redis
            stringRedisTemplate.opsForValue()
                    .set(AuthConstants.LOGIN_TOKEN_PREFIX + token,
                            usrJsonStr, Duration.ofSeconds(AuthConstants.TOKEN_TIME));
            //创建一个响应结果对象
            LoginResult loginResult = new LoginResult(token, AuthConstants.TOKEN_TIME);
            Result<Object> success = Result.success(loginResult);

            // 返回结果
            returnResult(response, success);
        };
    }

    /**
     * 登录失败处理器
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            Result<Object> result = new Result<>();
            result.setCode(BusinessEnum.OPERATION_FAIL.getCode());
            if (exception instanceof BadCredentialsException) {
                result.setMsg("用户名或密码错误");
            }
            else if (exception instanceof UsernameNotFoundException) {
                result.setMsg("用户不存在");
            } else if (exception instanceof AccountExpiredException) {
                result.setMsg("账号异常，请联系管理员");
            } else if (exception instanceof AccountStatusException) {
                result.setMsg("账号异常，请联系管理员");
            } else if (exception instanceof InternalAuthenticationServiceException) {
                result.setMsg(exception.getMessage());
            } else {
                result.setMsg(BusinessEnum.OPERATION_FAIL.getDesc());
            }
            returnResult(response, result);
        };
    }

    /**
     * 登出成功处理器
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            // 从请求头中获取token
            String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
            if (StringUtils.hasText(authorization)) {
                String token = authorization.replaceFirst(AuthConstants.BEARER, "");
                if (StringUtils.hasText(token)
                        && Boolean.TRUE.equals(stringRedisTemplate.hasKey(AuthConstants.LOGIN_TOKEN_PREFIX + token))) {
                    // 移除token
                    stringRedisTemplate.delete(AuthConstants.LOGIN_TOKEN_PREFIX + token);
                }
            }
           // 创建统一响应结果对象
            Result<Object> result = Result.success(null);
            returnResult(response, result);
        };
    }

    /**
     * 密码加密器
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
