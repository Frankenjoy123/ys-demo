package com.yunsoo.data.service.dao;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.joda.time.DateTime;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/2/9
 * Descriptions:
 */
public interface S3ItemDao {

    void putItem(String bucketName, String key, InputStream inputStream, ObjectMetadata objectMetadata);

    void putItem(String bucketName, String key, InputStream inputStream, ObjectMetadata objectMetadata, CannedAccessControlList cannedAccessControlList);

    S3Object getItem(String bucketName, String key);

    URL generatePresignedUrl(String bucketName, String key, DateTime expiration, HttpMethod method);

    boolean hasItem(String bucketName, String key);

    void deleteItem(String bucketName, String key);

    <T> void putItem(String bucketName, String key, T item);

    <T> T getItem(String bucketName, String key, Class<T> clazz);

    List<String> getItemNamesByFolderName(String bucketName, String folderName);
}
