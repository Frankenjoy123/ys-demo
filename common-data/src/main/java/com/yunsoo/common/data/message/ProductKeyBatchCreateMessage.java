package com.yunsoo.common.data.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.util.StringFormatter;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-04-27
 * Descriptions:
 */
public class ProductKeyBatchCreateMessage implements Serializable {

    public static final String PAYLOAD_TYPE = "product_key_batch_create";

    @JsonProperty("product_key_batch_id")
    private String productKeyBatchId;

    @JsonProperty("product_status_code")
    private String productStatusCode;

    @JsonProperty("continue_offset")
    private Integer continueOffset;


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

    public Integer getContinueOffset() {
        return continueOffset;
    }

    public void setContinueOffset(Integer continueOffset) {
        this.continueOffset = continueOffset;
    }

    @Override
    public String toString() {
        return StringFormatter.formatMap(
                "productKeyBatchId", productKeyBatchId,
                "productStatusCode", productStatusCode,
                "continueOffset", continueOffset);
    }
}
