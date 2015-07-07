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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Zhe on 2015/5/29.
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    private S3ItemDao s3ItemDao;

    @Override
    public S3Object getFile(String bucket, String key) throws IOException {
        try {
            S3Object item = s3ItemDao.getItem(bucket, key);
            return item;
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getErrorCode() == "NoSuchKey") {
                //log
            }
            return null;
        } catch (Exception ex) {
            //to-do: log
            return null;
        }
    }

    @Override
    public URL getPresignedUrl(String bucketName, String key, DateTime expiration) {
        return s3ItemDao.generatePresignedUrl(bucketName, key, expiration, HttpMethod.GET);
    }

    @Override
    public int uploadFile(String bucketName, String fullKey, FileObject fileObject, Boolean override) throws Exception {
        if (fileObject == null) throw new Exception("文件不能为空!");
        if (!override && s3ItemDao.hasItem(bucketName, fullKey)) return 0;

        InputStream inputStream = new ByteArrayInputStream(fileObject.getData());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        if (!fileObject.getContentType().isEmpty()) {
            objectMetadata.setContentType(fileObject.getContentType());
        }
        if (fileObject.getLength() != null) {
            objectMetadata.setContentLength(fileObject.getLength()); //set content-length
        }

        s3ItemDao.putItem(bucketName, fullKey, inputStream, objectMetadata, CannedAccessControlList.BucketOwnerFullControl);
        //to-do: move the old thumb into history folder.
        return 1;
    }
}
