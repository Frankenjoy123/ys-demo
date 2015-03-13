package com.yunsoo.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.web.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/3/13
 * Descriptions:
 */
public class ProductKey {

    private String productKey;
    private int productKeyTypeId;
    private boolean productKeyDisabled;
    private boolean primary;
    private int productKeyBatchId;
    private String primaryProductKey;
    private Set<String> productKeySet;
    private DateTime createdDateTime;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getProductKeyTypeId() {
        return productKeyTypeId;
    }

    public void setProductKeyTypeId(int productKeyTypeId) {
        this.productKeyTypeId = productKeyTypeId;
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

    public int getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(int productKeyBatchId) {
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

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
