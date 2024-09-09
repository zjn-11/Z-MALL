package com.zjn.mall.controller;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.zjn.mall.config.AliyunOSSConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;

/**
 * @author 张健宁
 * @ClassName FileUploadController
 * @Description 文件上传控制层
 * @createTime 2024年09月08日 22:29:00
 */

@Api(value = "文件上传控制层")
@RestController
@RequestMapping("admin/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final AliyunOSSConfig aliyunOSSConfig;

    /**
     * 上传文件：
     * 1. 要求必须是post请求
     * 2. 接收文件的对象类型必须是SpringMVC提供的MultipartFile
     * @return
     */
    @ApiOperation("上传单个文件")
    @PostMapping("upload/element")
    public String uploadFile(MultipartFile file) {
        String bucketName = aliyunOSSConfig.getBucketName();
        // 创建以天为单位的名称作为文件夹名称
        String newFolderName = DateUtil.format(new Date(), "yyyy-MM-dd");
        // 以时间戳作为文件名称
        String newFileName = DateUtil.format(new Date(), "HHmmssSSS");
        // 需要获取原文件的名称
        String originalFilename = file.getOriginalFilename();
        // 获取原文件后缀
        assert originalFilename != null;
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // 拼接得到存储对象的完整路径（不能包含bucketName）
        String objectName = newFolderName + "/" + newFileName + fileSuffix;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(aliyunOSSConfig.getEndpoint(), aliyunOSSConfig.getACCESS_KEY_ID(), aliyunOSSConfig.getACCESS_KEY_SECRET());
        URL url = null;
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file.getInputStream());
            // 上传文件
            ossClient.putObject(putObjectRequest);
            // 获取OSS返回的URL地址
            url = ossClient.generatePresignedUrl(bucketName, objectName, DateUtil.offsetDay(new Date(), 365 * 10));
        } catch (Exception oe) {
            System.out.println(oe.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        assert url != null;
        return url.toString();
    }
}
