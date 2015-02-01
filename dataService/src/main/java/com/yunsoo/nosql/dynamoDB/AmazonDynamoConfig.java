package com.yunsoo.nosql.dynamoDB;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
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
//@ComponentScan(basePackages = "com.yunsoo.nosql")
@PropertySource("classpath:amazon.dynamo.properties")
public class AmazonDynamoConfig {
    private static final String PROPERTY_NAME_AMAZON_DYNAMO_REGION = "amazon.dynamo.region";

    @Resource
    private Environment environment;

    @Bean
    AmazonDynamoDBClient amazonDynamoDBClient() throws AmazonClientException {
        AmazonDynamoDBClient dynamoDB;
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region region = Region.getRegion(Regions.fromName(environment.getProperty(PROPERTY_NAME_AMAZON_DYNAMO_REGION)));
        dynamoDB.setRegion(region);
        return dynamoDB;
    }

    @Bean
    DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDBClient());
    }

    @Bean
    AmazonDynamoRepo amazonDynamoRepo() {
        return new AmazonDynamoRepo();
    }
}
