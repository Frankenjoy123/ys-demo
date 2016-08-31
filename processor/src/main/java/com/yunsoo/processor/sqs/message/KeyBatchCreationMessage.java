package com.yunsoo.processor.sqs.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.util.StringFormatter;

/**
 * Created by:   Lijian
 * Created on:   2016-08-28
 * Descriptions:
 */
public class KeyBatchCreationMessage {

    public static final String PAYLOAD_TYPE = "key_batch_creation";

    @JsonProperty("key_batch_id")
    private String keyBatchId;

    @JsonProperty("product_status_code")
    private String productStatusCode;

    @JsonProperty("continue_offset")
    private Integer continueOffset;


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

    public Integer getContinueOffset() {
        return continueOffset;
    }

    public void setContinueOffset(Integer continueOffset) {
        this.continueOffset = continueOffset;
    }

    @Override
    public String toString() {
        return StringFormatter.formatMap(
                "keyBatchId", keyBatchId,
                "productStatusCode", productStatusCode,
                "continueOffset", continueOffset);
    }
}
