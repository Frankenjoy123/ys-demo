package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.config.AWSProperties;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

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
    private AWSProperties awsProperties;


    @RequestMapping(value = "s3", method = RequestMethod.GET)
    public ResponseEntity getS3FileByPath(@RequestParam(value = "path", required = true) String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        String bucketName = awsProperties.getS3().getBucketName();
        S3Object s3Object = fileService.getFile(bucketName, path);
        if (s3Object == null) {
            throw new NotFoundException("file not found [bucket: " + bucketName + ", path: " + path + "]");
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
        ByteArrayInputStream resultInputStream = new ByteArrayInputStream(StreamUtils.copyToByteArray(s3ObjectInputStream));
        s3ObjectInputStream.close();
        return bodyBuilder.body(new InputStreamResource(resultInputStream));
    }

    @RequestMapping(value = "s3", method = RequestMethod.PUT)
    public void putS3FileByPath(@RequestParam(value = "path", required = true) String path,
                                HttpServletRequest request) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        String bucketName = awsProperties.getS3().getBucketName();
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
        s3ItemDao.putItem(bucketName, path, inputStream, metadata);
    }

    @RequestMapping(value = "s3/list", method = RequestMethod.GET)
    public List<String> getS3FileNamesByFolderPath(@RequestParam(value = "path", required = true) String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        String bucketName = awsProperties.getS3().getBucketName();
        return fileService.getFileNamesByFolderName(bucketName, path);
    }

}
