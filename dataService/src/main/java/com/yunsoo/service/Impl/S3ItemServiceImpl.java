package com.yunsoo.service.Impl;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.dao.S3ItemDao;
import com.yunsoo.service.S3ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

/**
 * Created by Zhe on 2015/3/1.
 */
public class S3ItemServiceImpl implements S3ItemService {
    @Autowired
    public S3ItemDao s3ItemDao;

    @Override
    public void putFolderItem(String bucketName, String folderName) {
        s3ItemDao.putFolderItem(bucketName, folderName);
    }

    @Override
    public void putFileItem(String bucketName, String folderName, String fileName, File file, CannedAccessControlList cannedAccessControlList) {
        s3ItemDao.putFileItem(bucketName, folderName, fileName, file, cannedAccessControlList);
    }

    @Override
    public S3Object getItem(String bucketName, String key) {
        return s3ItemDao.getItem(bucketName, key);
    }

    @Override
    public boolean hasItem(String bucketName, String key) {
        return s3ItemDao.hasItem(bucketName, key);
    }

    @Override
    public void deleteItem(String bucketName, String key) {
        s3ItemDao.deleteItem(bucketName, key);

    }

    @Override
    public List<Bucket> getBuckets() {
        return s3ItemDao.getBuckets();
    }

    @Override
    public <T> void putItem(T item, String bucketName, String key) throws Exception {
        s3ItemDao.putItem(item, bucketName, key);
    }

    @Override
    public <T> T getItem(String bucketName, String key, Class<T> clazz) {
        return s3ItemDao.getItem(bucketName, key, clazz);
    }
}
