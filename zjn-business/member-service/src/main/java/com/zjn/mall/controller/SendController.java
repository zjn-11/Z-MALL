package com.zjn.mall.controller;

import com.zjn.mall.model.Result;
import com.zjn.mall.service.SendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 张健宁
 * @ClassName SendController
 * @Description 短信业务控制层
 * @createTime 2024年09月13日 17:03:00
 */

@Api("短信业务控制层")
@RestController
@RequestMapping("p/sms")
@RequiredArgsConstructor
public class SendController {

    private final SendService sendService;

    @ApiOperation("获取短信验证码")
    @PostMapping("send")
    public Result<String> sendPhoneMsg(@RequestBody Map<String, Object> map) throws Exception {
        sendService.sendPhoneMsg(map);
        return Result.success("短信发送成功");
    }
}
