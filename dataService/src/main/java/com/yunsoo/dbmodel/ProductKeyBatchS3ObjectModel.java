package com.yunsoo.dbmodel;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/15
 * Descriptions:
 */
public class ProductKeyBatchS3ObjectModel {

    private String id;
    private int quantity;
    private DateTime createdDateTime;
    private int[] productKeyTypeIds;
    private List<List<String>> productKeys;


    public ProductKeyBatchS3ObjectModel() {

    }

    public ProductKeyBatchS3ObjectModel(ProductKeyBatchModel productKeyBatchModel) {
        if (productKeyBatchModel != null) {
            this.id = productKeyBatchModel.getId();
            this.quantity = productKeyBatchModel.getQuantity();
            this.createdDateTime = productKeyBatchModel.getCreatedDateTime();
            this.productKeyTypeIds = productKeyBatchModel.getProductKeyTypeIds();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }


    public int[] getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(int[] productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
    }

    public List<List<String>> getProductKeys() {
        return productKeys;
    }

    public void setProductKeys(List<List<String>> productKeys) {
        this.productKeys = productKeys;
    }


}
