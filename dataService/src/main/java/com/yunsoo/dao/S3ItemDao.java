package com.yunsoo.dao;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.util.List;

/**
 * Created by Zhe on 2015/2/9.
 */
public interface S3ItemDao {

    public void putFolderItem(String bucketName, String folderName);

    public void putFileItem(String bucketName, String folderName, String fileName, File file, CannedAccessControlList cannedAccessControlList);

    public S3Object getItem(String bucketName, String key);

    public boolean hasItem(String bucketName, String key);

    public void deleteItem(String bucketName, String key);

    public List<Bucket> getBuckets();

    public <T> void putItem(T item, String bucketName, String key) throws Exception;

    public <T> T getItem(String bucketName, String key, Class<T> clazz);
}
