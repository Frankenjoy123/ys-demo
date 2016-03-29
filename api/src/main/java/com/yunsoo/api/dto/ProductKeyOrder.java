package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.ProductKeyTransactionObject;
import org.joda.time.DateTime;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/6
 * Descriptions:
 */
public class ProductKeyOrder {

    @JsonProperty("id")
    private String id;

    @JsonProperty("org_id")
    private String orgId;

    @Min(value = 1, message = "额度必须大于0")
    @JsonProperty("total")
    private Long total;

    @JsonProperty("remain")
    private Long remain;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonProperty("created_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;

    @JsonProperty("expire_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime expireDateTime;

    @JsonProperty("product_base")
    private ProductBase productBase;

    @JsonProperty("transaction_list")
    private List<ProductKeyTransactionObject> transactionList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getRemain() {
        return remain;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public DateTime getExpireDateTime() {
        return expireDateTime;
    }

    public void setExpireDateTime(DateTime expireDateTime) {
        this.expireDateTime = expireDateTime;
    }

    public List<ProductKeyTransactionObject> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<ProductKeyTransactionObject> transactionList) {
        this.transactionList = transactionList;
    }

    public ProductBase getProductBase() {
        return productBase;
    }

    public void setProductBase(ProductBase productBase) {
        this.productBase = productBase;
    }
}
