package com.yunsoo.key.dto;

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
 * Created on:   2016-08-19
 * Descriptions:
 */
public class Keys implements Serializable {

    @JsonProperty("batch_id")
    private String batchId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("key_type_codes")
    private List<String> keyTypeCodes;

    @JsonProperty("keys")
    private List<List<String>> keys;

    @JsonProperty("created_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
