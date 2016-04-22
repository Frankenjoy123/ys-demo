package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.api.dto.detectable.OrgIdDetectable;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.ProductObject;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by  : Zhe
 * Created on  : 2015/2/27
 * Descriptions:
 */
public class Product implements OrgIdDetectable {

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("product_key_type_code")
    private String productKeyTypeCode;

    @JsonProperty("product_key_batch_id")
    private String productKeyBatchId;

    @JsonProperty("product_key_set")
    private Set<String> productKeySet;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("status_code")
    private String productStatusCode;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("manufacturing_datetime")
    private DateTime manufacturingDateTime;

    @JsonProperty("details")
    private String details;


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductKeyTypeCode() {
        return productKeyTypeCode;
    }

    public void setProductKeyTypeCode(String productKeyTypeCode) {
        this.productKeyTypeCode = productKeyTypeCode;
    }

    public String getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(String productKeyBatchId) {
        this.productKeyBatchId = productKeyBatchId;
    }

    public Set<String> getProductKeySet() {
        return productKeySet;
    }

    public void setProductKeySet(Set<String> productKeySet) {
        this.productKeySet = productKeySet;
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

    public String getProductStatusCode() {
        return productStatusCode;
    }

    public void setProductStatusCode(String productStatusCode) {
        this.productStatusCode = productStatusCode;
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

    public Product() {
    }

    public Product(ProductObject productObject) {
        if (productObject != null) {
            this.setProductKey(productObject.getProductKey());
            this.setProductKeyTypeCode(productObject.getProductKeyTypeCode());
            this.setProductKeyBatchId(productObject.getProductKeyBatchId());
            this.setProductKeySet(productObject.getProductKeySet());
            this.setCreatedDateTime(productObject.getCreatedDateTime());
            this.setProductBaseId(productObject.getProductBaseId());
            this.setProductStatusCode(productObject.getProductStatusCode());
            this.setManufacturingDateTime(productObject.getManufacturingDateTime());
            this.setDetails(productObject.getDetails());
        }
    }
}
