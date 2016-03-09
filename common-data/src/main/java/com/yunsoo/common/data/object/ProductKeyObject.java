package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/3/16
 * Descriptions:
 */
public class ProductKeyObject implements Serializable {

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
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    private String details;


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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
