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

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("key_batch_id")
    private String keyBatchId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("key_type_codes")
    private List<String> keyTypeCodes;

    @JsonProperty("serial_no_pattern")
    private String serialNoPattern;

    @JsonProperty("keys")
    private List<List<String>> keys;

    @JsonProperty("created_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getKeyBatchId() {
        return keyBatchId;
    }

    public void setKeyBatchId(String keyBatchId) {
        this.keyBatchId = keyBatchId;
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

    public String getSerialNoPattern() {
        return serialNoPattern;
    }

    public void setSerialNoPattern(String serialNoPattern) {
        this.serialNoPattern = serialNoPattern;
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
