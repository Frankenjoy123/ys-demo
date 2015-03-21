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
    private Boolean productKeyDisabled;
    private int productKeyBatchId;
    private String primaryProductKey; //if primaryProductKey is null, then it's a primary item.
    private Set<String> productKeySet; //only exists when primaryProductKey is null

    private Long createdDateTimeValue;

    //only a primary item can contain below product info
    private Integer productBaseId;
    private Integer productStatusId;
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

    @DynamoDBAttribute(attributeName = "key_type_id") //product_key_type_id
    public int getProductKeyTypeId() {
        return productKeyTypeId;
    }

    public void setProductKeyTypeId(int productKeyTypeId) {
        this.productKeyTypeId = productKeyTypeId;
    }

    @DynamoDBAttribute(attributeName = "key_disabled") //product_key_disabled
    public Boolean getProductKeyDisabled() {
        return productKeyDisabled;
    }

    public void setProductKeyDisabled(Boolean productKeyDisabled) {
        this.productKeyDisabled = productKeyDisabled;
    }

    @DynamoDBAttribute(attributeName = "key_batch_id") //product_key_batch_id
    public int getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(int productKeyBatchId) {
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
    public Integer getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(Integer productBaseId) {
        this.productBaseId = productBaseId;
    }


    @DynamoDBAttribute(attributeName = "status_id") //product_status_id
    public Integer getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(Integer productStatusId) {
        this.productStatusId = productStatusId;
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
