package com.yunsoo.di.plugin;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.di.dao.s3.S3ItemDao;
import com.yunsoo.di.service.S3FileService;

import java.io.IOException;

/**
 * Created by yqy09_000 on 2016/11/3.
 * 只用于处理kettle相关的逻辑
 */
public class S3FileHandler {

    S3FileService fileService;
    public S3FileHandler(String region, String bucketName)
    {
        AmazonS3Client amazonS3Client = new AmazonS3Client();
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
        fileService = new S3FileService(new S3ItemDao(amazonS3Client), bucketName);
    }

    public YSFile getYSFile(String orgId, String fileId) throws IOException {
        String path = String.format("organization/%s/task_file/%s", orgId, fileId);
        ResourceInputStream inputStream = fileService.getFileByPath(path);
        return YSFile.read(inputStream);
    }
}
