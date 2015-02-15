package com.yunsoo.dbmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.yunsoo.amazon.dynamoDB.ArrayMarshaller;
import org.joda.time.DateTime;

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
 * "product_key_set_address": "S3 address"
 * }
 */
@DynamoDBTable(tableName = "product_key_batch")
public class ProductKeyBatchModel {

    private String id;
    private int quantity;
    private int statusId;
    private int createdClientId;
    private int createdAccountId;
    private long createdDateTimeValue;
    private int[] productKeyTypeIds;
    private String productKeysAddress;

    @DynamoDBHashKey(attributeName = "id") //id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "status_id") //status_id
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @DynamoDBAttribute(attributeName = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    @DynamoDBIgnore
    public DateTime getCreatedDateTime() {
        return new DateTime(createdDateTimeValue);
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTimeValue = createdDateTime.getMillis();
    }

    @DynamoDBAttribute(attributeName = "c_dt") //created_datetime
    public long getCreatedDateTimeValue() {
        return createdDateTimeValue;
    }

    public void setCreatedDateTimeValue(long createdDateTimeValue) {
        this.createdDateTimeValue = createdDateTimeValue;
    }

    @DynamoDBAttribute(attributeName = "key_type_ids") //product_key_type_ids
    @DynamoDBMarshalling(marshallerClass = ArrayMarshaller.class)
    public int[] getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(int[] productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
    }

    @DynamoDBAttribute(attributeName = "keys_add") //product_keys_address
    public String getProductKeysAddress() {
        return productKeysAddress;
    }

    public void setProductKeysAddress(String productKeySetAddress) {
        this.productKeysAddress = productKeySetAddress;
    }

}
