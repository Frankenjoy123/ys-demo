package com.yunsoo.dbmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/1/30
 * Descriptions:
 * <p>
 * "product_key": {
 * "product_key": "",
 * "key_type_id": "product_key_type.id",
 * "disabled": "disabled (=boolean)",
 * "batch_id": "product_key_batch.id",
 * "primary_product_key": "product.product_key",
 * "product_key_set": ["product_key.product_key (?primary_product_key)"]
 * }
 */
@DynamoDBTable(tableName = "product_key")
public class ProductKeyModel {

    private String productKey;
    private int productKeyTypeId;
    private boolean disabled;
    private String batchId;
    private String primaryProductKey;
    private Set<String> productKeySet;


    @DynamoDBHashKey(attributeName = "key") //product_key
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @DynamoDBAttribute(attributeName = "key_type_id") //product_key_type_id
    public int getProductKeyTypeId() {
        return productKeyTypeId;
    }

    public void setProductKeyTypeId(int productKeyTypeId) {
        this.productKeyTypeId = productKeyTypeId;
    }

    @DynamoDBAttribute(attributeName = "disabled") //disabled
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @DynamoDBAttribute(attributeName = "batch_id") //batch_id
    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    @DynamoDBAttribute(attributeName = "primary_key") //primary_product_key
    public String getPrimaryProductKey() {
        return primaryProductKey;
    }

    public void setPrimaryProductKey(String primaryProductKey) {
        this.primaryProductKey = primaryProductKey;
    }

    @DynamoDBAttribute(attributeName = "key_set") //product_key_set
    public Set<String> getProductKeySet() {
        return productKeySet;
    }

    public void setProductKeySet(Set<String> productKeySet) {
        this.productKeySet = productKeySet;
    }

}
