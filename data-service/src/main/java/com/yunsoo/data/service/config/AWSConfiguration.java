package com.yunsoo.data.service.config;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by:   Lijian
 * Created on:   2015/8/6
 * Descriptions:
 */
@Configuration
@Import(AWSProperties.class)
public class AWSConfiguration {

    @Value("${yunsoo.environment}")
    private String environment;

    @Autowired
    private AWSProperties awsProperties;

    @Bean
    public AmazonS3Client amazonS3Client() {
        String region = awsProperties.getRegion();
        AmazonS3Client amazonS3Client = new AmazonS3Client();
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
        return amazonS3Client;
    }

}
