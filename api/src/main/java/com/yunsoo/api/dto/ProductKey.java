package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/3/13
 * Descriptions:
 */
public class ProductKey {

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("product_key_type_code")
    private String productKeyTypeCode;

    @JsonProperty("product_key_disabled")
    private boolean productKeyDisabled;

    @JsonProperty("primary")
    private boolean primary;

    @JsonProperty("product_key_batch_id")
    private String productKeyBatchId;

    @JsonProperty("primary_product_key")
    private String primaryProductKey;

    @JsonProperty("product_key_set")
    private Set<String> productKeySet;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductKeyTypeCode() {
        return productKeyTypeCode;
    }

    public void setProductKeyTypeCode(String productKeyTypeCode) {
        this.productKeyTypeCode = productKeyTypeCode;
    }

    public boolean isProductKeyDisabled() {
        return productKeyDisabled;
    }

    public void setProductKeyDisabled(boolean productKeyDisabled) {
        this.productKeyDisabled = productKeyDisabled;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(String productKeyBatchId) {
        this.productKeyBatchId = productKeyBatchId;
    }

    public String getPrimaryProductKey() {
        return primaryProductKey;
    }

    public void setPrimaryProductKey(String primaryProductKey) {
        this.primaryProductKey = primaryProductKey;
    }

    public Set<String> getProductKeySet() {
        return productKeySet;
    }

    public void setProductKeySet(Set<String> productKeySet) {
        this.productKeySet = productKeySet;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ProductKey() {
    }

    public ProductKey(ProductKeyObject object) {
        if (object != null) {
            this.setProductKey(object.getProductKey());
            this.setProductKeyTypeCode(object.getProductKeyTypeCode());
            this.setProductKeyDisabled(object.isProductKeyDisabled());
            this.setPrimary(object.isPrimary());
            this.setProductKeyBatchId(object.getProductKeyBatchId());
            this.setPrimaryProductKey(object.getPrimaryProductKey());
            this.setProductKeySet(object.getProductKeySet());
            this.setCreatedDateTime(object.getCreatedDateTime());
        }
    }
}
