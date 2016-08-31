package com.yunsoo.api.rabbit.key.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public class Product implements Serializable {

    @JsonProperty("key")
    private String key;

    @JsonProperty("key_type_code")
    private String keyTypeCode;

    @JsonProperty("key_batch_id")
    private String keyBatchId;

    @JsonProperty("key_set")
    private Set<String> keySet;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("manufacturing_datetime")
    private DateTime manufacturingDateTime;

    @JsonProperty("details")
    private String details;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyTypeCode() {
        return keyTypeCode;
    }

    public void setKeyTypeCode(String keyTypeCode) {
        this.keyTypeCode = keyTypeCode;
    }

    public String getKeyBatchId() {
        return keyBatchId;
    }

    public void setKeyBatchId(String keyBatchId) {
        this.keyBatchId = keyBatchId;
    }

    public Set<String> getKeySet() {
        return keySet;
    }

    public void setKeySet(Set<String> keySet) {
        this.keySet = keySet;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public DateTime getManufacturingDateTime() {
        return manufacturingDateTime;
    }

    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        this.manufacturingDateTime = manufacturingDateTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
