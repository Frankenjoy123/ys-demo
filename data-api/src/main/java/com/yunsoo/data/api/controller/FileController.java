package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.S3FileObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/29
 * Descriptions:
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private S3ItemDao s3ItemDao;

    @Autowired
    private String s3BucketName;


    @RequestMapping(value = "s3", method = RequestMethod.GET)
    public ResponseEntity getS3FileByPath(@RequestParam(value = "path", required = true) String path) {
        if (path.isEmpty()) {
            throw new BadRequestException("path must not be null or empty");
        }
        S3Object s3Object = fileService.getFile(s3BucketName, path);
        if (s3Object == null) {
            throw new NotFoundException("file not found");
        }
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        ObjectMetadata metadata = s3Object.getObjectMetadata();
        String contentType = metadata.getContentType();
        long contentLength = metadata.getContentLength();

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        if (contentType != null) {
            bodyBuilder.contentType(MediaType.parseMediaType(contentType));
        }
        if (contentLength > 0) {
            bodyBuilder.contentLength(contentLength);
        }
        return bodyBuilder.body(new InputStreamResource(s3ObjectInputStream));
    }

    @RequestMapping(value = "s3", method = RequestMethod.PUT)
    public void putS3FileByPath(@RequestParam(value = "path", required = true) String path,
                                HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        long contentLength = request.getContentLengthLong();
        ServletInputStream inputStream = request.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        if (contentType != null) {
            metadata.setContentType(contentType);
        }
        if (contentLength > 0) {
            metadata.setContentLength(contentLength);
        }
        s3ItemDao.putItem(s3BucketName, path, inputStream, metadata);
    }

    @Deprecated
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getS3File(@RequestParam(value = "path", required = true) String path) throws Exception {
        if (path.isEmpty()) {
            throw new BadRequestException("Path未指定！");
        }

        try {
            S3Object s3Object = fileService.getFile(s3BucketName, path);
            if (s3Object == null) throw new NotFoundException("找不到S3File!");

            FileObject fileObject = new FileObject();
            fileObject.setContentType(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<>(fileObject, HttpStatus.OK);

        } catch (IOException ex) {
            throw new InternalServerErrorException("文件获取出错！");
        }
    }

    @Deprecated
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void updateS3File(@RequestBody FileObject fileObject) throws Exception {
        if (fileObject.getS3Path().isEmpty()) {
            throw new BadRequestException("文件上传Path未指定！");
        }
        try {
            fileObject.setContentType(fileObject.getContentType());
            fileService.putFile(s3BucketName, fileObject.getS3Path(), fileObject, true);

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
