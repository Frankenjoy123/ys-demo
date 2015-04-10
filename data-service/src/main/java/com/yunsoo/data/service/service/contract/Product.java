package com.yunsoo.data.service.service.contract;


import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/1/16
 * Descriptions:
 */
public class Product {

    private String productKey;
    private Long productBaseId;
    private int productStatusId;
    private DateTime manufacturingDateTime;
    private DateTime createdDateTime;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public Long getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(Long productBaseId) {
        this.productBaseId = productBaseId;
    }

    public int getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(int productStatusId) {
        this.productStatusId = productStatusId;
    }

    public DateTime getManufacturingDateTime() {
        return manufacturingDateTime;
    }

    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        this.manufacturingDateTime = manufacturingDateTime;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

}
