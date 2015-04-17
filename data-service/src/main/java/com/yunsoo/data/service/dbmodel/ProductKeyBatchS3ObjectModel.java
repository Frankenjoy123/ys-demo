package com.yunsoo.data.service.dbmodel;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/15
 * Descriptions:
 */
public class ProductKeyBatchS3ObjectModel {

    private Long id;
    private Integer quantity;
    private String createdDateTime;
    private List<String> productKeyTypeCodes;
    private List<List<String>> productKeys;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public List<String> getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }

    public List<List<String>> getProductKeys() {
        return productKeys;
    }

    public void setProductKeys(List<List<String>> productKeys) {
        this.productKeys = productKeys;
    }


}
