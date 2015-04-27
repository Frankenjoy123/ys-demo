package com.yunsoo.common.data.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2015/4/2
 * Descriptions:
 */
public class ProductKeyBatchMassage {

    @JsonProperty("product_key_batch_id")
    private String productKeyBatchId;

    @JsonProperty("product_status_code")
    private String productStatusCode;

    public String getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(String productKeyBatchId) {
        this.productKeyBatchId = productKeyBatchId;
    }

    public String getProductStatusCode() {
        return productStatusCode;
    }

    public void setProductStatusCode(String productStatusCode) {
        this.productStatusCode = productStatusCode;
    }

    @Override
    public String toString() {
        return "{productKeyBatchId: " + productKeyBatchId + ", productStatusCode: " + productStatusCode + "}";
    }
}
