package com.yunsoo.dbmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Date;

/**
 * Created by:   Lijian
 * Created on:   2015/1/29
 * Descriptions:
 * <p>
 * "product": {
 * "product_key": "",
 * "base_product_id": "base_product.id",
 * "status_id": "product_status.id",
 * "manufacturing_datetime": "2015-01-23T12:34:56:789Z",
 * "created_datetime": "2015-01-23T12:34:56:789Z"
 * }
 */
@DynamoDBTable(tableName = "product")
public class ProductModel {

    private String productKey;
    private int baseProductId;
    private int statusId;
    private Date manufacturingDateTime;
    private Date createdDateTime;

    @DynamoDBHashKey(attributeName = "key") //product_key
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @DynamoDBAttribute(attributeName = "base_product_id") //base_product_id
    public int getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(int baseProductId) {
        this.baseProductId = baseProductId;
    }

    @DynamoDBAttribute(attributeName = "status_id") //status_id
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @DynamoDBAttribute(attributeName = "mf_dt") //manufacturing_datetime
    public Date getManufacturingDateTime() {
        return manufacturingDateTime;
    }

    public void setManufacturingDateTime(Date manufacturingDateTime) {
        this.manufacturingDateTime = manufacturingDateTime;
    }

    @DynamoDBAttribute(attributeName = "c_dt") //create_datetime
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDate) {
        this.createdDateTime = createdDate;
    }

}
