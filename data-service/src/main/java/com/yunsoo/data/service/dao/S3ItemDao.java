package com.yunsoo.data.service.dao;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/2/9
 * Descriptions:
 */
public interface S3ItemDao {

//    void putFolderItem(String bucketName, String folderName);
//
//    void putFileItem(String bucketName, String folderName, String fileName, File file, CannedAccessControlList cannedAccessControlList);

    void putItem(String bucketName, String key, InputStream inputStream, ObjectMetadata objectMetadata);

    void putItem(String bucketName, String key, InputStream inputStream, ObjectMetadata objectMetadata, CannedAccessControlList cannedAccessControlList);

    S3Object getItem(String bucketName, String key);

    boolean hasItem(String bucketName, String key);

    void deleteItem(String bucketName, String key);

    List<Bucket> getBuckets();

    <T> void putItem(String bucketName, String key, T item);

    <T> T getItem(String bucketName, String key, Class<T> clazz);
}
