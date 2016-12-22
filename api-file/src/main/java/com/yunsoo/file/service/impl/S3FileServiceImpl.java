package com.yunsoo.file.service.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.file.dao.S3ItemDao;
import com.yunsoo.file.service.FileService;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public class S3FileServiceImpl implements FileService {

    private S3ItemDao s3ItemDao;

    private String bucketName;


    public S3FileServiceImpl(S3ItemDao s3ItemDao, String bucketName) {
        this.s3ItemDao = s3ItemDao;
        this.bucketName = bucketName;
    }


    @Override
    public ResourceInputStream getFileByPath(String path, String bucketName) {
        if(!StringUtils.hasText(bucketName))
            bucketName = this.bucketName;
        S3Object s3Object;
        try {
            s3Object = s3ItemDao.getItem(bucketName, path);
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getStatusCode() == 404) {
                return null;
            } else {
                throw s3ex;
            }
        }
        if (s3Object == null) {
            return null;
        }
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        ObjectMetadata metadata = s3Object.getObjectMetadata();
        long contentLength = metadata.getContentLength();
        String contentType = metadata.getContentType();

        try {
            ByteArrayInputStream resultInputStream = new ByteArrayInputStream(StreamUtils.copyToByteArray(s3ObjectInputStream));
            return new ResourceInputStream(resultInputStream, contentLength, contentType);
        } catch (IOException e) {
            throw new RuntimeException("get file failed with IOException", e);
        }
    }

    @Override
    public void putFileToPath(String path, ResourceInputStream inputStream) {
        long contentLength = inputStream.getContentLength();
        String contentType = inputStream.getContentType();
        ObjectMetadata metadata = new ObjectMetadata();
        if (contentType != null) {
            metadata.setContentType(contentType);
        }
        if (contentLength > 0) {
            metadata.setContentLength(contentLength);
        }
        s3ItemDao.putItem(bucketName, path, inputStream, metadata);
    }

    @Override
    public List<String> getFileNamesByFolderName(String folderName){
        try {
            List<String> nameList = s3ItemDao.getItemNamesByFolderName(bucketName, folderName);
            return nameList.stream().map(this::getFileShortName).collect(Collectors.toList());
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getStatusCode() == 404) {
                return new ArrayList<>();
            } else {
                throw s3ex;
            }
        }
    }

    private String getFileShortName(String fileFullName){
        String[] names = fileFullName.split("/");
        return names[names.length -1];
    }


}
