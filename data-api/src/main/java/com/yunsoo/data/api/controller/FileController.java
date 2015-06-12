package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.S3FileObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.data.service.config.AmazonSetting;
import com.yunsoo.data.service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

/**
 * Created by Zhe on 2015/5/29.
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private AmazonSetting amazonSetting;

    //upload resource to S3 by full path
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void updateS3File(@RequestBody FileObject fileObject) throws Exception {
        if (fileObject.getS3Path().isEmpty()) {
            throw new BadRequestException("文件上传Path未指定！");
        }
        try {
            fileObject.setContentType(fileObject.getContentType());
            int result = fileService.uploadFile(amazonSetting.getS3_basebucket(), fileObject.getS3Path(), fileObject, true);

        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("上传文件失败！ Message = " + e.getMessage());
        }
    }

    @RequestMapping(value = "/tempURL", method = RequestMethod.POST)
    public URL getPresignedURL(@RequestBody S3FileObject s3FileObject) {

//        if (key == null || key.isEmpty()) throw new BadRequestException("Key不能为空！");
        return fileService.getPresignedUrl(s3FileObject.getBucketName(), s3FileObject.getKey(), s3FileObject.getExpiration());
    }
}
