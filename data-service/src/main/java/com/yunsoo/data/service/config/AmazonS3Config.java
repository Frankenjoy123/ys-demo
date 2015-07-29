package com.yunsoo.data.service.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/2/6
 * Descriptions:
 */
@Configuration
public class AmazonS3Config {

    @Value("${yunsoo.aws.region}")
    private String region;

    @Value("${yunsoo.aws.s3.bucket_name}")
    private String s3BucketName;

    @Bean
    public AmazonS3Client amazonS3Client() {
        AmazonS3Client amazonS3Client = new AmazonS3Client();
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
        return amazonS3Client;
    }

    @Bean
    public String s3BucketName() {
        return s3BucketName;
    }
}
