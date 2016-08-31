package com.yunsoo.file.service.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.yunsoo.file.dao.S3ItemDao;
import com.yunsoo.file.service.FileService;
import com.yunsoo.file.service.impl.S3FileServiceImpl;
import com.yunsoo.file.service.impl.SimpleFileServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
@Configuration
public class StorageConfiguration {

    @Configuration
    @ConditionalOnProperty(value = "yunsoo.storage.type", havingValue = "s3")
    public static class S3StorageConfiguration {

        @Value("${yunsoo.storage.s3.region:cn-north-1}")
        private String region;

        @Value("${yunsoo.storage.s3.bucket_name}")
        private String bucketName;


        @Bean
        public FileService fileService() {
            AmazonS3Client amazonS3Client = new AmazonS3Client();
            amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
            return new S3FileServiceImpl(new S3ItemDao(amazonS3Client), bucketName);
        }

    }

    @Configuration
    @ConditionalOnProperty(value = "yunsoo.storage.type", havingValue = "simple")
    public static class SimpleStorageConfiguration {

        @Value("${yunsoo.storage.simple.base_path}")
        private String basePath;

        @Bean
        public FileService fileService() {
            return new SimpleFileServiceImpl(basePath);
        }

    }

}
