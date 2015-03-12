package com.yunsoo.dbmodel;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/15
 * Descriptions:
 */
public class ProductKeyBatchS3ObjectModel {

    private int id;
    private int quantity;
    private String createdDateTime;
    private List<Integer> productKeyTypeIds;
    private List<List<String>> productKeys;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }


    public List<Integer> getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(List<Integer> productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
    }

    public List<List<String>> getProductKeys() {
        return productKeys;
    }

    public void setProductKeys(List<List<String>> productKeys) {
        this.productKeys = productKeys;
    }


}
