package com.yunsoo.dbmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/1/29
 * Descriptions:
 * <p>
 * "product": {
 * "product_key": "(String(22))",
 * <p>
 * "product_key_type_id": "product_key_type.id",
 * "product_key_disabled": "(boolean)",
 * "product_key_batch_id": "product_key_batch.id",
 * "primary_product_key": "product.product_key(String(22))",
 * "product_key_set": ["product.product_key(String(22))"],
 * "created_datetime": "(long)",
 * <p>
 * "product_base_id": "product_base.id(int)",
 * "product_status_id": "product_status.id(int)",
 * "manufacturing_datetime": "(long)"
 * }
 */
@DynamoDBTable(tableName = "product")
public class ProductModel {

    private String productKey;

    private int productKeyTypeId;
    private boolean productKeyDisabled;
    private String productKeyBatchId;
    private String primaryProductKey; //if primaryProductKey is null, then it's a primary item.
    private Set<String> productKeySet; //only exists when primaryProductKey is null

    private long createdDateTimeValue;

    //only a primary item can contain below product info
    private int productBaseId;
    private int productStatusId;
    private long manufacturingDateTimeValue;


    @DynamoDBHashKey(attributeName = "key") //product_key
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @DynamoDBIgnore
    public boolean isPrimary() {
        return primaryProductKey == null || primaryProductKey.equals(productKey);
    }

    @DynamoDBAttribute(attributeName = "key_type_id") //product_key_type_id
    public int getProductKeyTypeId() {
        return productKeyTypeId;
    }

    public void setProductKeyTypeId(int productKeyTypeId) {
        this.productKeyTypeId = productKeyTypeId;
    }

    @DynamoDBAttribute(attributeName = "key_disabled") //product_key_disabled
    public boolean isProductKeyDisabled() {
        return productKeyDisabled;
    }

    public void setProductKeyDisabled(boolean productKeyDisabled) {
        this.productKeyDisabled = productKeyDisabled;
    }

    @DynamoDBAttribute(attributeName = "key_batch_id") //product_key_batch_id
    public String getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(String productKeyBatchId) {
        this.productKeyBatchId = productKeyBatchId;
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

    @DynamoDBIgnore
    public DateTime getCreatedDateTime() {
        return createdDateTimeValue > 0 ? new DateTime(createdDateTimeValue) : null;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        if (createdDateTime != null) {
            this.createdDateTimeValue = createdDateTime.getMillis();
        }
    }

    @DynamoDBAttribute(attributeName = "c_dt") //created_datetime
    public long getCreatedDateTimeValue() {
        return createdDateTimeValue;
    }

    public void setCreatedDateTimeValue(long createdDateTimeValue) {
        this.createdDateTimeValue = createdDateTimeValue;
    }


    @DynamoDBAttribute(attributeName = "product_base_id") //product_base_id
    public int getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(int productBaseId) {
        this.productBaseId = productBaseId;
    }


    @DynamoDBAttribute(attributeName = "status_id") //product_status_id
    public int getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(int productStatusId) {
        this.productStatusId = productStatusId;
    }


    @DynamoDBIgnore
    public DateTime getManufacturingDateTime() {
        return manufacturingDateTimeValue > 0 ? new DateTime(manufacturingDateTimeValue) : null;
    }

    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        if (manufacturingDateTime != null) {
            this.manufacturingDateTimeValue = manufacturingDateTime.getMillis();
        }
    }

    @DynamoDBAttribute(attributeName = "mf_dt") //manufacturing_datetime
    public long getManufacturingDateTimeValue() {
        return createdDateTimeValue;
    }

    public void setManufacturingDateTimeValue(long createdDateTimeValue) {
        this.createdDateTimeValue = createdDateTimeValue;
    }
}
