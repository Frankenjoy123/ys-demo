package com.yunsoo.api.object;

import org.joda.time.DateTime;

/**
 * Created by Zhe on 2015/2/27.
 */
public class TProduct {

    private String productKey;
    private int productBaseId;
    private int productStatusId;
    private String manufacturingDateTime;
    private String createdDateTime;

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

    public int getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(int productStatusId) {
        this.productStatusId = productStatusId;
    }

    public String getManufacturingDateTime() {
        return manufacturingDateTime;
    }

    public void setManufacturingDateTime(String manufacturingDateTime) {
        this.manufacturingDateTime = manufacturingDateTime;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
