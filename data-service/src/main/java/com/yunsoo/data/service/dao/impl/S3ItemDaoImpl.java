package com.yunsoo.data.service.dao.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.data.service.dao.S3ItemDao;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by  : Zhe
 * Created on  : 2015/2/9
 * Descriptions:
 */
@Repository("s3ItemDao")
public class S3ItemDaoImpl implements S3ItemDao {

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    AmazonS3Client amazonS3Client;

    @Override
    public void putItem(String bucketName, String key, InputStream inputStream, ObjectMetadata objectMetadata) {
        this.putItem(bucketName, key, inputStream, objectMetadata, CannedAccessControlList.BucketOwnerFullControl);
    }

    @Override
    public void putItem(String bucketName, String key, InputStream inputStream, ObjectMetadata objectMetadata, CannedAccessControlList cannedAccessControlList) {
        objectMetadata = objectMetadata == null ? new ObjectMetadata() : objectMetadata;
        amazonS3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata)
                .withCannedAcl(cannedAccessControlList));
    }

    @Override
    public S3Object getItem(String bucketName, String key) {
        return amazonS3Client.getObject(new GetObjectRequest(bucketName, key));
    }

    @Override
    public URL generatePresignedUrl(String bucketName, String key, DateTime expiration, HttpMethod method) {
        return amazonS3Client.generatePresignedUrl(bucketName, key, expiration.toDate(), method);
    }

    @Override
    public boolean hasItem(String bucketName, String key) {
        return getItem(bucketName, key) != null;
    }

    @Override
    public void deleteItem(String bucketName, String key) {
        amazonS3Client.deleteObject(bucketName, key);
    }

    @Override
    public <T> void putItem(String bucketName, String key, T item) {
        try {
            byte[] buf = mapper.writeValueAsBytes(item);
            InputStream inputStream = new ByteArrayInputStream(buf);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/json");
            amazonS3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Note: putItem failed! " + "[bucketName: " + bucketName + ", key: " + key + "]", ex);
        }
    }

    @Override
    public <T> T getItem(String bucketName, String key, Class<T> clazz) {
        S3Object object = this.getItem(bucketName, key);
        if (object == null) return null;
        try {
            return mapper.readValue(object.getObjectContent(), clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Note: getItem failed! " + "[bucketName: " + bucketName + ", key: " + key + "]", ex);
        }
    }
}
