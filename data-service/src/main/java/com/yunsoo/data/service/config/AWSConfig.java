package com.yunsoo.data.service.config;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/8/6
 * Descriptions:
 */
@Configuration
public class AWSConfig {

    @Value("${yunsoo.environment}")
    private String environment;

    @Autowired
    private AWSConfigProperties awsConfigProperties;

    @Bean
    public AmazonDynamoDBClient amazonDynamoDBClient() throws AmazonClientException {
        AmazonDynamoDBClient dynamoDB = new AmazonDynamoDBClient();
        String endpoint = awsConfigProperties.getDynamoDB() != null ? awsConfigProperties.getDynamoDB().getEndpoint() : null;
        String region = awsConfigProperties.getRegion();

        if (endpoint != null && endpoint.length() > 0) {
            dynamoDB.setEndpoint(endpoint);
        } else if (region != null) {
            dynamoDB.setRegion(Region.getRegion(Regions.fromName(region)));
        }

        return dynamoDB;
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        DynamoDBMapperConfig config = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(getDynamoDBTableNamePrefix()))
                .build();

        return new DynamoDBMapper(amazonDynamoDBClient(), config);
    }

    private String getDynamoDBTableNamePrefix() {
        return environment + "-";
    }

    @Bean
    public AmazonS3Client amazonS3Client() {
        String region = awsConfigProperties.getRegion();
        AmazonS3Client amazonS3Client = new AmazonS3Client();
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
        return amazonS3Client;
    }

}
