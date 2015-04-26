package com.yunsoo.processor.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2015/4/2
 * Descriptions:
 */
public class ProductKeyBatchMassage {

    @JsonProperty("product_key_batch_id")
    private String productKeyBatchId;

    public String getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(String productKeyBatchId) {
        this.productKeyBatchId = productKeyBatchId;
    }

    @Override
    public String toString() {
        return "{productKeyBatchId: " + productKeyBatchId + "}";
    }
}
