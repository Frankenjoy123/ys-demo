package com.yunsoo.key.service.config;

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
 * Created on:   2016-08-18
 * Descriptions:
 */
@Configuration
public class StorageConfiguration {

    @Configuration
    public static class DynamodbStorageConfiguration {

        @Value("${yunsoo.storage.dynamodb.region:cn-north-1}")
        private String region;

        @Value("${yunsoo.storage.dynamodb.table_prefix:}")
        private String tablePrefix;

        @Value("${yunsoo.storage.dynamodb.endpoint:}")
        private String endpoint;


        @Bean
        public AmazonDynamoDBClient amazonDynamoDBClient() throws AmazonClientException {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient();

            if (endpoint != null && endpoint.length() > 0) {
                client.setEndpoint(endpoint);
            } else {
                client.setRegion(Region.getRegion(Regions.fromName(region)));
            }

            return client;
        }

        @Bean
        public DynamoDBMapper dynamoDBMapper() {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig.Builder()
                    .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(tablePrefix))
                    .build();

            return new DynamoDBMapper(amazonDynamoDBClient(), config);
        }

    }

}
