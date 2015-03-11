package com.yunsoo.dao.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.dao.S3ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.List;

/**
 * Created by Zhe on 2015/2/9.
 */
@Repository("s3ItemDao")
public class S3ItemDaoImpl implements S3ItemDao {
    private static final String SUFFIX = "/";
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    AmazonS3Client amazonS3Client;

    @Override
    public void putFolderItem(String bucketName, String folderName) {

        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                folderName + SUFFIX, emptyContent, metadata);
        // send request to S3 to create folder
        amazonS3Client.putObject(putObjectRequest);
    }

    // upload file to folder and set CannedAccessControlList
    @Override
    public void putFileItem(String bucketName, String folderName, String fileName, File file, CannedAccessControlList cannedAccessControlList) {

        String newFileName = folderName + SUFFIX + fileName;
        amazonS3Client.putObject(new PutObjectRequest(bucketName, newFileName, file)
                .withCannedAcl(cannedAccessControlList));
    }

    //upload item with inputstream
    @Override
    public void putItem(String bucketName, String objectPath, InputStream inputStream, ObjectMetadata objectMetadata, CannedAccessControlList cannedAccessControlList) {
        objectMetadata = objectMetadata == null ? new ObjectMetadata() : objectMetadata;
        amazonS3Client.putObject(new PutObjectRequest(bucketName, objectPath, inputStream, objectMetadata)
                .withCannedAcl(cannedAccessControlList));
    }

    @Override
    public S3Object getItem(String bucketName, String key) {
        return amazonS3Client.getObject(new GetObjectRequest(bucketName, key));
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
    public List<Bucket> getBuckets() {
        return amazonS3Client.listBuckets();
    }

    @Override
    public <T> void putItem(T item, String bucketName, String key) {
        try {
            byte[] buf = mapper.writeValueAsBytes(item);
            InputStream inputStream = new ByteArrayInputStream(buf);
            //to-do: replace null with new ObjectMetadata() ?
            amazonS3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, null));
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
