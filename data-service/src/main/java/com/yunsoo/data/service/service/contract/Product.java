package com.yunsoo.data.service.service.contract;


import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/1/16
 * Descriptions:
 */
public class Product {

    private String productKey;

    private String productKeyTypeCode;

    private String productKeyBatchId;

    private Set<String> productKeySet;

    private DateTime createdDateTime;

    private String productBaseId;

    private String productStatusCode;

    private DateTime manufacturingDateTime;

    private String details;


    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductKeyTypeCode() {
        return productKeyTypeCode;
    }

    public void setProductKeyTypeCode(String productKeyTypeCode) {
        this.productKeyTypeCode = productKeyTypeCode;
    }

    public String getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(String productKeyBatchId) {
        this.productKeyBatchId = productKeyBatchId;
    }

    public Set<String> getProductKeySet() {
        return productKeySet;
    }

    public void setProductKeySet(Set<String> productKeySet) {
        this.productKeySet = productKeySet;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getProductStatusCode() {
        return productStatusCode;
    }

    public void setProductStatusCode(String productStatusCode) {
        this.productStatusCode = productStatusCode;
    }

    public DateTime getManufacturingDateTime() {
        return manufacturingDateTime;
    }

    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        this.manufacturingDateTime = manufacturingDateTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
