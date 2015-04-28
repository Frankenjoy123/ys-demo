package com.yunsoo.api.dto;

import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/16
 * Descriptions:
 */
public class ProductKeyBatchRequest {

    @Range(min = 1, max = 100000, message = "单次申请数量必须大于1并且不超过100000")
    private int quantity;

    private List<String> productKeyTypeCodes;

    private String productBaseId;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }
}
