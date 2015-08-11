package com.yunsoo.data.service.service.Impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.data.service.dao.S3ItemDao;
import com.yunsoo.data.service.service.FileService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/29
 * Descriptions:
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    private S3ItemDao s3ItemDao;

    @Override
    public S3Object getFile(String bucketName, String key) {
        try {
            return s3ItemDao.getItem(bucketName, key);
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getStatusCode() == 404) {
                return null;
            } else {
                throw s3ex;
            }
        }
    }

    @Override
    public void putFile(String bucketName, String key, FileObject fileObject, Boolean override) {
        if (fileObject == null) throw new RuntimeException("file must not be null");
        if (!override && s3ItemDao.hasItem(bucketName, key)) throw new RuntimeException("file already exits");

        InputStream inputStream = new ByteArrayInputStream(fileObject.getData());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (!fileObject.getContentType().isEmpty()) {
            objectMetadata.setContentType(fileObject.getContentType());
        }
        if (fileObject.getLength() != null) {
            objectMetadata.setContentLength(fileObject.getLength()); //set content-length
        }

        s3ItemDao.putItem(bucketName, key, inputStream, objectMetadata, CannedAccessControlList.BucketOwnerFullControl);

    }

    @Override
    public URL getPresignedUrl(String bucketName, String key, DateTime expiration) {
        return s3ItemDao.generatePresignedUrl(bucketName, key, expiration, HttpMethod.GET);
    }

}
