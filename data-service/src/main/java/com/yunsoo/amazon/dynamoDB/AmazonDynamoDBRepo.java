package com.yunsoo.amazon.dynamoDB;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by:   Lijian
 * Created on:   2015/1/27
 * Descriptions:
 */
public class AmazonDynamoDBRepo {

    @Autowired
    private AmazonDynamoDBClient amazonDynamoDBClient;

    public TableDescription getTableDescription(Class<?> typeOfDynamoItem) {
        DescribeTableRequest describeTableRequest = new DescribeTableRequest()
                .withTableName(getTableNameFromModel(typeOfDynamoItem));
        return this.amazonDynamoDBClient.describeTable(describeTableRequest).getTable();
    }

    private String getTableNameFromModel(Class<?> model) {
        return model.getAnnotation(DynamoDBTable.class).tableName();
    }

}
