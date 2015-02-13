package com.yunsoo.amazon.s3;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Created by:   Lijian
 * Created on:   2015/2/6
 * Descriptions:
 */
@Configuration
@PropertySource("classpath:amazon.properties")
public class AmazonS3Config {
    private static final String PROPERTY_NAME_AMAZON_S3_PROFILE = "amazon.s3.profile";
    private static final String PROPERTY_NAME_AMAZON_S3_REGION = "amazon.s3.region";

    @Resource
    private Environment environment;

    @Bean
    public AmazonS3Client amazonS3Client() {
        AmazonS3Client amazonS3Client = null;
        String profileName = environment.getProperty(PROPERTY_NAME_AMAZON_S3_PROFILE);
        String regionName = environment.getProperty(PROPERTY_NAME_AMAZON_S3_REGION);
        amazonS3Client = new AmazonS3Client(new ProfileCredentialsProvider(profileName));
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(regionName)));
        return amazonS3Client;
    }
}
