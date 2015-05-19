package com.yunsoo.data.service.dbmodel.dynamodb;

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
 * "product_key": "(String(22))",
 * "product_key_type_code": "product_key_type.code",
 * "product_key_disabled": "(Boolean)",
 * "product_key_batch_id": "product_key_batch.id(String)",
 * "primary_product_key": "product.product_key(String(22))",
 * "product_key_set": ["product.product_key(String(22))"],
 * "created_datetime": "(Long)",
 * "product_base_id": "product_base.id(String)",
 * "product_status_code": "product_status.code(String)",
 * "manufacturing_datetime": "(Long)"
 * }
 */
@DynamoDBTable(tableName = "product")
public class ProductModel {

    private String productKey;

    private String productKeyTypeCode;
    private Boolean productKeyDisabled;
    private String productKeyBatchId;
    private String primaryProductKey; //if primaryProductKey is null, then it's a primary item.
    private Set<String> productKeySet; //only exists when primaryProductKey is null
    private Long createdDateTimeValue;

    //only a primary item can contain below product info
    private String productBaseId;
    private String productStatusCode;
    private Long manufacturingDateTimeValue;


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

    @DynamoDBAttribute(attributeName = "key_type_code") //product_key_type_code
    public String getProductKeyTypeCode() {
        return productKeyTypeCode;
    }

    public void setProductKeyTypeCode(String productKeyTypeCode) {
        this.productKeyTypeCode = productKeyTypeCode;
    }

    @DynamoDBAttribute(attributeName = "key_disabled") //product_key_disabled
    public Boolean getProductKeyDisabled() {
        return productKeyDisabled;
    }

    public void setProductKeyDisabled(Boolean productKeyDisabled) {
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
        return createdDateTimeValue != null ? new DateTime(createdDateTimeValue) : null;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        if (createdDateTime != null) {
            this.createdDateTimeValue = createdDateTime.getMillis();
        }
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


    @DynamoDBIgnore
    public DateTime getManufacturingDateTime() {
        return manufacturingDateTimeValue != null ? new DateTime(manufacturingDateTimeValue) : null;
    }

    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        if (manufacturingDateTime != null) {
            this.manufacturingDateTimeValue = manufacturingDateTime.getMillis();
        }
    }

    @DynamoDBAttribute(attributeName = "mfg_dt") //manufacturing_datetime
    public Long getManufacturingDateTimeValue() {
        return manufacturingDateTimeValue;
    }

    public void setManufacturingDateTimeValue(Long manufacturingDateTimeValue) {
        this.manufacturingDateTimeValue = manufacturingDateTimeValue;
    }
}
