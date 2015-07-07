package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/16
 * Descriptions:
 */
public class ProductKeyBatch {

    @JsonProperty("id")
    private String id;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status")
    private ProductKeyBatchStatus status;

    @JsonProperty("product_key_type_codes")
    private List<String> productKeyTypeCodes;

    @JsonProperty("product_key_types")
    private List<ProductKeyType> productKeyTypes;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("created_app_id")
    private String createdAppId;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;


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

    public ProductKeyBatchStatus getStatus() {
        return status;
    }

    public void setStatus(ProductKeyBatchStatus status) {
        this.status = status;
    }

    public List<String> getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }

    public List<ProductKeyType> getProductKeyTypes() {
        return productKeyTypes;
    }

    public void setProductKeyTypes(List<ProductKeyType> productKeyTypes) {
        this.productKeyTypes = productKeyTypes;
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
}
