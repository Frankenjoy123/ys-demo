package com.yunsoo.key.dao.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/1/30
 * Descriptions:
 * "product_package": {
 * "product_key": "product.product_key",
 * "parent_product_key": "product.product_key",
 * "child_product_key_set": ["product.product_key"],
 * "disabled": "(Boolean)",
 * "package_datetime": "(Long)"
 * }
 */
@DynamoDBTable(tableName = "product_package")
public class ProductPackageModel {

    private String productKey;
    private String parentProductKey;
    private Set<String> childProductKeySet;
    private Boolean disabled;
    private Long packageDateTimeValue;


    @DynamoDBHashKey(attributeName = "key") //product_key
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @DynamoDBAttribute(attributeName = "parent_key") //parent_product_key
    public String getParentProductKey() {
        return parentProductKey;
    }

    public void setParentProductKey(String parentProductKey) {
        this.parentProductKey = parentProductKey;
    }

    @DynamoDBAttribute(attributeName = "child_key_set") //child_product_key_set
    public Set<String> getChildProductKeySet() {
        return childProductKeySet;
    }

    public void setChildProductKeySet(Set<String> childProductKeySet) {
        this.childProductKeySet = childProductKeySet;
    }

    @DynamoDBAttribute(attributeName = "disabled") //disabled
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @DynamoDBAttribute(attributeName = "pkg_dt") //package_datetime
    public Long getPackageDateTimeValue() {
        return packageDateTimeValue;
    }

    public void setPackageDateTimeValue(Long packageDateTimeValue) {
        this.packageDateTimeValue = packageDateTimeValue;
    }


    @DynamoDBIgnore
    public boolean isDisabled() {
        return this.disabled != null && this.disabled;
    }

    @DynamoDBIgnore
    public DateTime getPackageDateTime() {
        return packageDateTimeValue != null ? new DateTime(packageDateTimeValue) : null;
    }

    public void setPackageDateTime(DateTime createdDateTime) {
        if (createdDateTime != null) {
            this.packageDateTimeValue = createdDateTime.getMillis();
        }
    }

    public void appendChildProductKey(String key) {
        if (key != null && key.length() > 0) {
            if (this.childProductKeySet == null) {
                this.childProductKeySet = new HashSet<>();
            }
            this.childProductKeySet.add(key);
        }
    }

    public void appendChildProductKey(Set<String> keySet) {
        if (keySet != null && keySet.size() > 0) {
            if (this.childProductKeySet == null) {
                this.childProductKeySet = new HashSet<>();
            }
            for (String k : keySet) {
                if (k != null && k.length() > 0) {
                    this.childProductKeySet.add(k);
                }
            }
        }
    }

    @DynamoDBIgnore
    public boolean canBeOverrideOn(DateTime packageDateTime) {
        return packageDateTimeValue == null || (packageDateTime != null && packageDateTimeValue <= packageDateTime.getMillis());
    }
}
