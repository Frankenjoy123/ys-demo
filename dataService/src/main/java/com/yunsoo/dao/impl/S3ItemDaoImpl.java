package com.yunsoo.dao.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.dao.S3ItemDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Zhe on 2015/2/9.
 */
public class S3ItemDaoImpl implements S3ItemDao {
    private static final String SUFFIX = "/";

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

    @Override
    public S3Object getItem(String bucketName, String key) {
        S3Object object = amazonS3Client.getObject(new GetObjectRequest(bucketName, key));
        return object;
    }

    @Override
    public Boolean hasItem(String bucketName, String key) {
        return getItem(bucketName, key) != null;
    }

    @Override
    public void deleteItem(String bucketName, String key) {
        amazonS3Client.deleteObject(bucketName, key);
    }

    @Override
    public List<Bucket> listItem(String bucketName) {
        return amazonS3Client.listBuckets();

    }

    @Override
    public AmazonS3Client getClients() {
        return amazonS3Client;
    }
}
