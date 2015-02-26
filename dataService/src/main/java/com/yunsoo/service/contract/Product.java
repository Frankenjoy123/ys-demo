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
    private int baseProductId;
    private int productStatusId;
    private DateTime manufacturingDateTime;
    private DateTime createdDateTime;
    private BaseProduct baseProduct;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
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

    public BaseProduct getBaseProduct() {
        return baseProduct;
    }

    public void setBaseProduct(BaseProduct baseProduct) {
        this.baseProduct = baseProduct;
    }

}
