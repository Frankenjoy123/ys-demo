package com.yunsoo.dao;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Zhe on 2015/2/9.
 */
public interface S3ItemDao {

    public void putFolderItem(String bucketName, String folderName);

    public void putFileItem(String bucketName, String folderName, String fileName, File file, CannedAccessControlList cannedAccessControlList);

    public S3Object getItem(String bucketName, String key);

    public Boolean hasItem(String bucketName, String key);

    public void deleteItem(String bucketName, String key);

    public List<Bucket> listItem(String bucketName);

    public AmazonS3Client getClients();

    public <T> void putItem(T item, String bucketName, String key);

    public <T> T getItem(String bucketName, String key, Class<T> clazz);
}
