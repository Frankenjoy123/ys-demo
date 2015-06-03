package com.yunsoo.api.rabbit.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Zhe on 2015/4/8.
 */
public class OperationRecord {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("digest")
    private String digest;
    @JsonProperty("result")
    private String result;
    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("operation_type")
    private int operationType;
    @JsonProperty("created_datetime")
    private String createdDateTime;
    @JsonProperty("location")
    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
