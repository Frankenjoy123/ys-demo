package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.S3FileObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.config.AmazonSetting;
import com.yunsoo.data.service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getS3File(@RequestParam(value = "path", required = true) String path) throws Exception {
        if (path.isEmpty()) {
            throw new BadRequestException("Path未指定！");
        }

        try {
            S3Object s3Object = fileService.getFile(amazonSetting.getS3_basebucket(), path);
            if (s3Object == null) throw new NotFoundException("找不到S3File!");

            FileObject fileObject = new FileObject();
            fileObject.setContentType(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);

        } catch (IOException ex) {
            throw new InternalServerErrorException("文件获取出错！");
        }
    }

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
        return fileService.getPresignedUrl(s3FileObject.getBucketName(), s3FileObject.getKey(), s3FileObject.getExpiration());
    }
}
