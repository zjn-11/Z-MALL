package com.zjn.mall.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.zjn.mall.AliyunDxProperties;
import com.zjn.mall.constants.MemberConstants;
import com.zjn.mall.service.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

/**
 * @author 张健宁
 * @ClassName SendServiceImpl
 * @Description TODO
 * @createTime 2024年09月13日 17:07:00
 */
@Service
@RequiredArgsConstructor
public class SendServiceImpl implements SendService {

    private final StringRedisTemplate redisTemplate;
    private final AliyunDxProperties aliyunDxProperties;

    @Override
    public void sendPhoneMsg(Map<String, Object> map) {

        // 初始化短信服务配置
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(aliyunDxProperties.getAccessKeyId())
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(aliyunDxProperties.getAccessKeySecret());
        // Endpoint
        config.endpoint = aliyunDxProperties.getEndpoint();

        // 创建服务端
        Client client = null;
        try {
            client = new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 发送的号码
        String phonenum = (String) map.get("phonenum");
        // 签名
        String signName = aliyunDxProperties.getSignName();
        // 生成随机的验证码
        String randomCode = RandomUtil.randomNumbers(4);
        // 将验证码注入到redis中
        redisTemplate.opsForValue().set(
                MemberConstants.PHONE_PREFIX +  phonenum,
                randomCode, Duration.ofMinutes(30));
        // 设置模版参数
        String templateParam = "{\"code\":\"" +randomCode+"\"}";

        // 创建请求参数
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(phonenum)
                .setSignName(signName)
                .setTemplateCode(aliyunDxProperties.getTemplateCode())
                .setTemplateParam(templateParam);
        try {
            // 发送短信
            client.sendSmsWithOptions(sendSmsRequest, new com.aliyun.teautil.models.RuntimeOptions());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
