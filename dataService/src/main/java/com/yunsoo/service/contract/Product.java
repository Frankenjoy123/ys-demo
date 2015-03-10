package com.yunsoo.service.contract;


import org.joda.time.DateTime;

/**
 * Created by Lijian on 2015/1/16.
 * <p>
 * Update by Zhe 2015/1/26
 * Import Base Product property,
 * Add FromModel ToModelList etc as converter methods.
 */
public class Product {

    private String productKey;
    private int productBaseId;
    private int statusId;
    private DateTime manufacturingDateTime;
    private DateTime createdDateTime;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(int productBaseId) {
        this.productBaseId = productBaseId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
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
