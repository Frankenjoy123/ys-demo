package com.yunsoo.file.dao;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-17
 * Descriptions:
 */
public class S3ItemDao {

    private static ObjectMapper mapper = new ObjectMapper();

    AmazonS3Client amazonS3Client;

    public S3ItemDao(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public void putItem(String bucketName, String key, InputStream inputStream, ObjectMetadata objectMetadata) {
        this.putItem(bucketName, key, inputStream, objectMetadata, CannedAccessControlList.BucketOwnerFullControl);
    }

    public void putItem(String bucketName, String key, InputStream inputStream, ObjectMetadata objectMetadata, CannedAccessControlList cannedAccessControlList) {
        objectMetadata = objectMetadata == null ? new ObjectMetadata() : objectMetadata;
        amazonS3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata)
                .withCannedAcl(cannedAccessControlList));
    }

    public S3Object getItem(String bucketName, String key) {
        return amazonS3Client.getObject(new GetObjectRequest(bucketName, key));
    }

    public URL generatePresignedUrl(String bucketName, String key, DateTime expiration, HttpMethod method) {
        return amazonS3Client.generatePresignedUrl(bucketName, key, expiration.toDate(), method);
    }

    public boolean hasItem(String bucketName, String key) {
        return getItem(bucketName, key) != null;
    }

    public void deleteItem(String bucketName, String key) {
        amazonS3Client.deleteObject(bucketName, key);
    }

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

    public <T> T getItem(String bucketName, String key, Class<T> clazz) {
        S3Object object = this.getItem(bucketName, key);
        if (object == null) return null;
        try {
            return mapper.readValue(object.getObjectContent(), clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Note: getItem failed! " + "[bucketName: " + bucketName + ", key: " + key + "]", ex);
        }
    }

    public List<String> getItemNamesByFolderName(String bucketName, String folderName) {
        ObjectListing objects = amazonS3Client.listObjects(bucketName, folderName);
        return objects.getObjectSummaries().stream().map(this::getKey).collect(Collectors.toList());

    }

    private String getKey(S3ObjectSummary objectSummary) {
        return objectSummary.getKey();
    }
}
