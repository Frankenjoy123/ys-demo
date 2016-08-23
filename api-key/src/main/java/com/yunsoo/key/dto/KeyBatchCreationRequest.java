package com.yunsoo.key.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-23
 * Descriptions:
 */
public class KeyBatchCreationRequest implements Serializable {

    @JsonProperty("partition_id")
    private String partitionId;

    @JsonProperty("batch_no")
    private String batchNo;

    @Range(min = 1, max = 1000000, message = "quantity must in range of 1 to 1000000")
    @JsonProperty("quantity")
    private Integer quantity;

    @NotEmpty(message = "key_type_codes must not be null or empty")
    @JsonProperty("key_type_codes")
    private List<String> keyTypeCodes;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @NotEmpty(message = "org_id must not be null or empty")
    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("created_app_id")
    private String createdAppId;

    @JsonProperty("created_device_id")
    private String createdDeviceId;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonProperty("external_keys")
    private List<String> externalKeys;


    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreatedAppId() {
        return createdAppId;
    }

    public void setCreatedAppId(String createdAppId) {
        this.createdAppId = createdAppId;
    }

    public String getCreatedDeviceId() {
        return createdDeviceId;
    }

    public void setCreatedDeviceId(String createdDeviceId) {
        this.createdDeviceId = createdDeviceId;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public List<String> getExternalKeys() {
        return externalKeys;
    }

    public void setExternalKeys(List<String> externalKeys) {
        this.externalKeys = externalKeys;
    }
}
