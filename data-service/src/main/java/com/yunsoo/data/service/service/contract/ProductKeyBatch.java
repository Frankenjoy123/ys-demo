package com.yunsoo.data.service.service.contract;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/5
 * Descriptions:
 */
public class ProductKeyBatch {

    private String id;
    private Integer quantity;
    private String statusCode;
    private String orgId;
    private String productBaseId;
    private String createdClientId;
    private String createdAccountId;
    private DateTime createdDateTime;
    private List<String> productKeyTypeCodes;
    private String productKeysUri;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getCreatedClientId() {
        return createdClientId;
    }

    public void setCreatedClientId(String createdClientId) {
        this.createdClientId = createdClientId;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public List<String> getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }

    public String getProductKeysUri() {
        return productKeysUri;
    }

    public void setProductKeysUri(String productKeysUri) {
        this.productKeysUri = productKeysUri;
    }
}
