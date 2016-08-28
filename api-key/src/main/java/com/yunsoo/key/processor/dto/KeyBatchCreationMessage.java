package com.yunsoo.key.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-08-23
 * Descriptions:
 */
public class KeyBatchCreationMessage implements Serializable {

    @JsonProperty("key_batch_id")
    private String keyBatchId;

    @JsonProperty("product_status_code")
    private String productStatusCode;


    public String getKeyBatchId() {
        return keyBatchId;
    }

    public void setKeyBatchId(String keyBatchId) {
        this.keyBatchId = keyBatchId;
    }

    public String getProductStatusCode() {
        return productStatusCode;
    }

    public void setProductStatusCode(String productStatusCode) {
        this.productStatusCode = productStatusCode;
    }

}
