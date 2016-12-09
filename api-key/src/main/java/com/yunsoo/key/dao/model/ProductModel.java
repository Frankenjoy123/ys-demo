package com.yunsoo.key.dao.model;

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
 * "product": {
 * "key": "(String(22))",
 * "key_type_code": "key_type.code",
 * "key_disabled": "(Boolean)",
 * "key_batch_id": "key_batch.id(String)",
 * "primary_key": "product.key(String(22))",
 * "key_set": ["product.key(String(22))"],
 * "created_datetime": "(Long)",
 * "product_base_id": "product_base.id(String)",
 * "product_status_code": "product_status.code(String)",
 * "manufacturing_datetime": "(Long)"
 * }
 */
@DynamoDBTable(tableName = "product")
public class ProductModel {

    private String key;

    private String keyTypeCode;
    private Boolean keyDisabled;
    private String keyBatchId;
    private String primaryKey; //if primaryKey is null, then it's a primary item.
    private Set<String> keySet; //only exists when primaryKey is null
    private Long createdDateTimeValue;

    //only a primary item can contain below product info
    private String productBaseId;
    private String productStatusCode;
    private Long manufacturingDateTimeValue;
    private String serialNo;
    private String details;


    @DynamoDBHashKey(attributeName = "key") //key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @DynamoDBAttribute(attributeName = "key_type_code") //key_type_code
    public String getKeyTypeCode() {
        return keyTypeCode;
    }

    public void setKeyTypeCode(String keyTypeCode) {
        this.keyTypeCode = keyTypeCode;
    }

    @DynamoDBAttribute(attributeName = "key_disabled") //key_disabled
    public Boolean getKeyDisabled() {
        return keyDisabled;
    }

    public void setKeyDisabled(Boolean keyDisabled) {
        this.keyDisabled = keyDisabled;
    }

    @DynamoDBAttribute(attributeName = "key_batch_id") //key_batch_id
    public String getKeyBatchId() {
        return keyBatchId;
    }

    public void setKeyBatchId(String keyBatchId) {
        this.keyBatchId = keyBatchId;
    }

    @DynamoDBAttribute(attributeName = "primary_key") //primary_key
    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @DynamoDBAttribute(attributeName = "key_set") //key_set
    public Set<String> getKeySet() {
        return keySet;
    }

    public void setKeySet(Set<String> keySet) {
        this.keySet = keySet;
    }

    @DynamoDBAttribute(attributeName = "created_dt") //created_datetime
    public Long getCreatedDateTimeValue() {
        return createdDateTimeValue;
    }

    public void setCreatedDateTimeValue(Long createdDateTimeValue) {
        this.createdDateTimeValue = createdDateTimeValue;
    }

    @DynamoDBAttribute(attributeName = "base_id") //product_base_id
    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    @DynamoDBAttribute(attributeName = "status_code") //product_status_code
    public String getProductStatusCode() {
        return productStatusCode;
    }

    public void setProductStatusCode(String productStatusCode) {
        this.productStatusCode = productStatusCode;
    }

    @DynamoDBAttribute(attributeName = "mfg_dt") //manufacturing_datetime
    public Long getManufacturingDateTimeValue() {
        return manufacturingDateTimeValue;
    }

    public void setManufacturingDateTimeValue(Long manufacturingDateTimeValue) {
        this.manufacturingDateTimeValue = manufacturingDateTimeValue;
    }

    @DynamoDBAttribute(attributeName = "sn") //serial no
    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @DynamoDBAttribute(attributeName = "details") //details
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    @DynamoDBIgnore
    public boolean isPrimary() {
        return primaryKey == null || primaryKey.equals(key);
    }

    @DynamoDBIgnore
    public boolean isKeyDisabled() {
        return this.keyDisabled != null && this.keyDisabled;
    }

    @DynamoDBIgnore
    public DateTime getCreatedDateTime() {
        return createdDateTimeValue != null ? new DateTime(createdDateTimeValue) : null;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        if (createdDateTime != null) {
            this.createdDateTimeValue = createdDateTime.getMillis();
        }
    }

    @DynamoDBIgnore
    public DateTime getManufacturingDateTime() {
        return manufacturingDateTimeValue != null ? new DateTime(manufacturingDateTimeValue) : null;
    }

    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        if (manufacturingDateTime != null) {
            this.manufacturingDateTimeValue = manufacturingDateTime.getMillis();
        }
    }
}
