package com.yunsoo.dbmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/1/29
 * Descriptions:
 * <p>
 * "product": {
 * "product_key": "",
 * "product_base_id": "product_base.id",
 * "status_id": "product_status.id",
 * "manufacturing_datetime": "(=long)",
 * "created_datetime": "(=long)"
 * }
 */
@DynamoDBTable(tableName = "product")
public class ProductModel {

    private String productKey;
    private int productBaseId;
    private int statusId;
    private long manufacturingDateTimeValue;
    private long createdDateTimeValue;


    @DynamoDBHashKey(attributeName = "key") //product_key
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }


    @DynamoDBAttribute(attributeName = "product_base_id") //product_base_id
    public int getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(int productBaseId) {
        this.productBaseId = productBaseId;
    }


    @DynamoDBAttribute(attributeName = "status_id") //status_id
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }


    @DynamoDBIgnore
    public DateTime getManufacturingDateTime() {
        return new DateTime(manufacturingDateTimeValue);
    }

    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        this.manufacturingDateTimeValue = manufacturingDateTime.getMillis();
    }

    @DynamoDBAttribute(attributeName = "mf_dt") //manufacturing_datetime
    public long getManufacturingDateTimeValue() {
        return createdDateTimeValue;
    }

    public void setManufacturingDateTimeValue(long createdDateTimeValue) {
        this.createdDateTimeValue = createdDateTimeValue;
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
}
