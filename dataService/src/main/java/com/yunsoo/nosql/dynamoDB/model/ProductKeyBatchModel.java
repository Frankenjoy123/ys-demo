package com.yunsoo.nosql.dynamoDB.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Date;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/1/30
 * Descriptions:
 * <p>
 * "product_key_batch": {
 * "id": "",
 * "status_id": "batch_status.id",
 * "created_client_id": "client.id",
 * "created_account_id": "account.id",
 * "created_datetime": "2015-01-23T12:34:56:789Z",
 * "product_key_set": "S3 address"
 * }
 */
@DynamoDBTable(tableName = "product_key_batch")
public class ProductKeyBatchModel {

    private long id;
    private int statusId;
    private int createdClientId;
    private int createdAccountId;
    private Date createdDateTime;
    private Set<String> productKeySet;

    @DynamoDBHashKey(attributeName = "id") //id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "status_id") //status_id
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @DynamoDBAttribute(attributeName = "c_client_id") //created_client_id
    public int getCreatedClientId() {
        return createdClientId;
    }

    public void setCreatedClientId(int createdClientId) {
        this.createdClientId = createdClientId;
    }

    @DynamoDBAttribute(attributeName = "c_account_id") //created_account_id
    public int getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(int createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    @DynamoDBAttribute(attributeName = "c_dt") //created_datetime
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @DynamoDBAttribute(attributeName = "key_set") //product_key_set
    public Set<String> getProductKeySet() {
        return productKeySet;
    }

    public void setProductKeySet(Set<String> productKeySet) {
        this.productKeySet = productKeySet;
    }

}
