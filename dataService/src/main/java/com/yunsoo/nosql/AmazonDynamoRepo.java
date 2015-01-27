package com.yunsoo.nosql;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/1/27
 * Descriptions:
 */
public class AmazonDynamoRepo {

    @Autowired
    private AmazonDynamoDBClient amazonDynamoDBClient;

    public void Test() {
        System.out.println(amazonDynamoDBClient);
    }

    public TableDescription getTableDescription(String tableName) {
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        return amazonDynamoDBClient.describeTable(describeTableRequest).getTable();
    }

    public void putItem(String tableName, Map<String, AttributeValue> item){

        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        PutItemResult putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
    }

}
