package com.yunsoo.nosql.dynamoDB;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Created by:   Lijian
 * Created on:   2015/1/27
 * Descriptions: Config AmazonDynamoDB related instances
 */
@Configuration
@PropertySource("classpath:amazon.properties")
public class AmazonDynamoDBConfig {
    private static final String PROPERTY_NAME_AMAZON_DYNAMODB_PROFILE = "amazon.dynamodb.profile";
    private static final String PROPERTY_NAME_AMAZON_DYNAMODB_REGION = "amazon.dynamodb.region";
    private static final String PROPERTY_NAME_AMAZON_DYNAMODB_ENDPOINT = "amazon.dynamodb.endpoint";

    @Resource
    private Environment environment;

    @Bean
    public AmazonDynamoDBClient amazonDynamoDBClient() throws AmazonClientException {
        AmazonDynamoDBClient dynamoDB;
        String profileName = environment.getProperty(PROPERTY_NAME_AMAZON_DYNAMODB_PROFILE);
        String regionName = environment.getProperty(PROPERTY_NAME_AMAZON_DYNAMODB_REGION);
        String endpoint = environment.getProperty(PROPERTY_NAME_AMAZON_DYNAMODB_ENDPOINT);

        dynamoDB = new AmazonDynamoDBClient(new ProfileCredentialsProvider(profileName));

        if (endpoint != null) {
            dynamoDB.setEndpoint(endpoint);
        } else if (regionName != null) {
            dynamoDB.setRegion(Region.getRegion(Regions.fromName(regionName)));
        }

        return dynamoDB;
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDBClient());
    }

    @Bean
    public AmazonDynamoDBRepo amazonDynamoDBRepo() {
        return new AmazonDynamoDBRepo();
    }

}
