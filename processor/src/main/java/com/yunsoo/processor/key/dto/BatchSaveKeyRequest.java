package com.yunsoo.processor.key.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
public class BatchSaveKeyRequest implements Serializable {

    @JsonProperty("key_batch_id")
    private String keyBatchId;

    @JsonProperty("key_type_codes")
    private List<String> keyTypeCodes;

    @JsonProperty("keys")
    private List<List<String>> keys;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("product_status_code")
    private String productStatusCode;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("manufacturing_datetime")
    private DateTime manufacturingDateTime;

    public String getKeyBatchId() {
        return keyBatchId;
    }

    public void setKeyBatchId(String keyBatchId) {
        this.keyBatchId = keyBatchId;
    }

    public List<String> getKeyTypeCodes() {
        return keyTypeCodes;
    }

    public void setKeyTypeCodes(List<String> keyTypeCodes) {
        this.keyTypeCodes = keyTypeCodes;
    }

    public List<List<String>> getKeys() {
        return keys;
    }

    public void setKeys(List<List<String>> keys) {
        this.keys = keys;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getProductStatusCode() {
        return productStatusCode;
    }

    public void setProductStatusCode(String productStatusCode) {
        this.productStatusCode = productStatusCode;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getManufacturingDateTime() {
        return manufacturingDateTime;
    }

    public void setManufacturingDateTime(DateTime manufacturingDateTime) {
        this.manufacturingDateTime = manufacturingDateTime;
    }
}
