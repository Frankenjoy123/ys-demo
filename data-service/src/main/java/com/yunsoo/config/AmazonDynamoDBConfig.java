package com.yunsoo.config;

import com.amazonaws.AmazonClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by:   Lijian
 * Created on:   2015/1/27
 * Descriptions: Config AmazonDynamoDB related instances
 */
@Configuration
public class AmazonDynamoDBConfig {

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
        System.out.println(region);
        System.out.println(endpoint);
        return dynamoDB;
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDBClient());
    }

}
