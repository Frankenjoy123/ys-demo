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
 * "product_package": {
 * "product_key": "product.product_key",
 * "child_product_key_set": ["product.product_key"],
 * "parent_product_key": "product.product_key",
 * "status_id": "product_package_status.id",
 * "created_datetime": "2015-01-23T12:34:56:789Z"
 * }
 */
@DynamoDBTable(tableName = "product_package")
public class ProductPackageModel {

    private String productKey;
    private Set<String> childProductKeySet;
    private String parentProductKey;
    private int statusId;
    private Date createdDateTime;


    @DynamoDBHashKey(attributeName = "key") //product_key
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @DynamoDBAttribute(attributeName = "child_key_set") //child_product_key_set
    public Set<String> getChildProductKeySet() {
        return childProductKeySet;
    }

    public void setChildProductKeySet(Set<String> childProductKeySet) {
        this.childProductKeySet = childProductKeySet;
    }

    @DynamoDBAttribute(attributeName = "parent_key") //parent_product_key
    public String getParentProductKey() {
        return parentProductKey;
    }

    public void setParentProductKey(String parentProductKey) {
        this.parentProductKey = parentProductKey;
    }

    @DynamoDBAttribute(attributeName = "status_id") //status_id
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @DynamoDBAttribute(attributeName = "c_dt") //created_datetime
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
