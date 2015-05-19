package com.yunsoo.data.service.config;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/1/27
 * Descriptions: Config AmazonDynamoDB related instances
 */
@Configuration
public class AmazonDynamoDBConfig {

    @Value("${yunsoo.environment}")
    private String environment;

    @Value("${yunsoo.aws.region}")
    private String region;

    @Value("${yunsoo.aws.dynamodb.endpoint}")
    private String endpoint;

    @Bean
    public AmazonDynamoDBClient amazonDynamoDBClient() throws AmazonClientException {
        AmazonDynamoDBClient dynamoDB = new AmazonDynamoDBClient();

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

}
