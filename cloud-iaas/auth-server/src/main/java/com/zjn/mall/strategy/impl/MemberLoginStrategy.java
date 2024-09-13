package com.zjn.mall.strategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zjn.mall.config.WxParamProperties;
import com.zjn.mall.constants.AuthConstants;
import com.zjn.mall.domain.LoginMember;
import com.zjn.mall.domain.Member;
import com.zjn.mall.mapper.LoginMemberMapper;
import com.zjn.mall.model.SecurityUser;
import com.zjn.mall.strategy.LoginStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 张健宁
 * @ClassName MemberLoginStrategy
 * @Description 商城购物系统登录具体实现
 * @createTime 2024年09月03日 19:35:00
 */
@Service(AuthConstants.MEMBER_LOGIN)
@RequiredArgsConstructor
public class MemberLoginStrategy implements LoginStrategy {

    private final WxParamProperties wxParamProperties;
    private final LoginMemberMapper loginMemberMapper;
    @Override
    public UserDetails realLogin(String username) {
        // 获取微信接口服务器的：登录凭证校验接口传入参数 -> （appid，assSecret，code）
        // 微信小程序服务器会返回session_key和openId
        String url = String.format(wxParamProperties.getUrl(),
                wxParamProperties.getAppid(),
                wxParamProperties.getSecret(),
                username);
        // 调用登录凭证校验接口获取返回的session_key和openId
        String jsonStr = HttpUtil.get(url);
        if (!StringUtils.hasText(jsonStr)) {
            throw new InternalAuthenticationServiceException("登录失败，请重试！");
        }
        // 将json转为对象
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        // 获取openId
        String openid = jsonObject.getString("openid");
        // 判断是否有openid
        if (!StringUtils.hasText(openid)) {
            throw new InternalAuthenticationServiceException("登录失败，请重试！");
        }
        // 根据openid查询会员对象
        LoginMember loginMember = loginMemberMapper.selectOne(
                new LambdaQueryWrapper<LoginMember>()
                        .eq(LoginMember::getOpenId, openid)
        );
        if (ObjectUtil.isNull(loginMember)) {
            // 不存在则进行注册
            loginMember = registerMember(openid);
        }
        // 判断会员状态，只能允许状态正常的账号登录
        if (!loginMember.getStatus().equals(1)) {
            throw new InternalAuthenticationServiceException("账号异常，请联系平台工作人员！");
        }
        // 返回可识别的UserDetails对象SecurityUser
        SecurityUser securityUser = new SecurityUser();
        securityUser.setLoginType(AuthConstants.MEMBER_LOGIN);
        securityUser.setUserId(loginMember.getId());
        securityUser.setUsername(openid);
        securityUser.setStatus(loginMember.getStatus());
        securityUser.setPassword(new BCryptPasswordEncoder().encode("WECHAT"));
        securityUser.setOpenid(openid);

        return securityUser;
    }

    private LoginMember registerMember(String openid) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = request.getRemoteAddr();
        LoginMember loginMember = new LoginMember();
        loginMember.setOpenId(openid);
        loginMember.setCreateTime(new Date());
        loginMember.setUpdateTime(new Date());
        loginMember.setStatus(1);
        loginMember.setUserLasttime(new Date());
        loginMember.setUserRegip(ip);
        loginMember.setUserLastip(ip);
        loginMember.setScore(0);

        // 新增会员信息
        loginMemberMapper.insert(loginMember);

        return loginMember;
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("WECHAT"));
    }
}
