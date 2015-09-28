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
import com.yunsoo.data.service.config.AWSProperties;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
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

    @Deprecated
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getS3File(@RequestParam(value = "path", required = true) String path) {
        if (StringUtils.isEmpty(path)) {
            throw new BadRequestException("path must not be null or empty");
        }
        String bucketName = awsProperties.getS3().getBucketName();
        try {
            S3Object s3Object = fileService.getFile(bucketName, path);
            if (s3Object == null) {
                throw new NotFoundException("file not found [bucket: " + bucketName + ", path: " + path + "]");
            }
            FileObject fileObject = new FileObject();
            fileObject.setContentType(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<>(fileObject, HttpStatus.OK);

        } catch (IOException ex) {
            throw new InternalServerErrorException();
        }
    }

    @Deprecated
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void updateS3File(@RequestBody FileObject fileObject) throws Exception {
        if (StringUtils.isEmpty(fileObject.getS3Path())) {
            throw new BadRequestException("path must not be null or empty");
        }
        String bucketName = awsProperties.getS3().getBucketName();
        try {
            fileObject.setContentType(fileObject.getContentType());
            fileService.putFile(bucketName, fileObject.getS3Path(), fileObject, true);

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
