package com.yunsoo.service.contract;

import java.util.Date;

/**
 * Created by Lijian on 2015/1/16.
 */
public class Product {

    private int Id;
    private int baseProductId;
    private int productStatusId;
    private Date manufacturingDate;
    private Date createdDateTime;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(int baseProductId) {
        this.baseProductId = baseProductId;
    }

    public int getProductStatusId() {
        return productStatusId;
    }

    public void setProductStatusId(int productStatusId) {
        this.productStatusId = productStatusId;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Date manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDate) {
        this.createdDateTime = createdDate;
    }
}
