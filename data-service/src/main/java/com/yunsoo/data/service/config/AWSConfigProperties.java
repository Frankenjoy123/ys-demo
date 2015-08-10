package com.yunsoo.data.service.config;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

/**
 * Created by:   Lijian
 * Created on:   2015/2/6
 * Descriptions:
 */
@Configuration
@EnableConfigurationProperties()
@ConfigurationProperties(prefix = "yunsoo.aws")
public class AWSConfigProperties {

    @NotNull(message = "region not configured")
    private String region;

    private DynamoDB dynamoDB;

    @NotNull(message = "s3 not configured")
    private S3 s3;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public DynamoDB getDynamoDB() {
        return dynamoDB;
    }

    public void setDynamoDB(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public S3 getS3() {
        return s3;
    }

    public void setS3(S3 s3) {
        this.s3 = s3;
    }


    public static class DynamoDB {

        private String endpoint;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }


    public static class S3 {

        @NotEmpty(message = "bucketName not configured")
        private String bucketName;

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }
    }

}
