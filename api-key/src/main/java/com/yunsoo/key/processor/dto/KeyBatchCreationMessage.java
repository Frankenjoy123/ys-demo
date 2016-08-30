package com.yunsoo.key.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-08-23
 * Descriptions:
 */
public class KeyBatchCreationMessage implements Serializable {

    public static final String PAYLOAD_TYPE = "key_batch_creation";

    @JsonProperty("key_batch_id")
    private String keyBatchId;


    public String getKeyBatchId() {
        return keyBatchId;
    }

    public void setKeyBatchId(String keyBatchId) {
        this.keyBatchId = keyBatchId;
    }

}
