package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import com.yunsoo.common.web.security.util.OrgIdDetectable;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/16
 * Descriptions:
 */
public class ProductKeyBatch implements OrgIdDetectable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("batch_no")
    private String batchNo;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status")
    private Lookup status;

    @JsonProperty("product_key_type_codes")
    private List<String> productKeyTypeCodes;

    @JsonProperty("product_key_types")
    private List<Lookup> productKeyTypes;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("created_app_id")
    private String createdAppId;

    @JsonProperty("created_device_id")
    private String createdDeviceId;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("marketing_id")
    private String marketingId;

    @JsonProperty("download_no")
    private Integer downloadNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Lookup getStatus() {
        return status;
    }

    public void setStatus(Lookup status) {
        this.status = status;
    }

    public List<String> getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }

    public List<Lookup> getProductKeyTypes() {
        return productKeyTypes;
    }

    public void setProductKeyTypes(List<Lookup> productKeyTypes) {
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

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public Integer getDownloadNo() {
        return downloadNo == null ? 0 : downloadNo;
    }

    public void setDownloadNo(Integer downloadNo) {
        this.downloadNo = downloadNo;
    }

    public ProductKeyBatch() {
    }

    public ProductKeyBatch(ProductKeyBatchObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setBatchNo(object.getBatchNo());
            this.setQuantity(object.getQuantity());
            this.setStatusCode(object.getStatusCode());
            this.setProductKeyTypeCodes(object.getProductKeyTypeCodes());
            this.setProductBaseId(object.getProductBaseId());
            this.setOrgId(object.getOrgId());
            this.setCreatedAppId(object.getCreatedAppId());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setMarketingId(object.getMarketingId());
            this.setDownloadNo(object.getDownloadNo());
        }
    }
}
