package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.api.dto.detectable.OrgIdDetectable;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.MarketingObject;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2015/1/25
 * Descriptions:
 */
public class Marketing implements OrgIdDetectable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("wishes")
    private String wishes;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("balance")
    private Double balance;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("modified_account_id")
    private String modifiedAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;

    @JsonProperty("product_base_name")
    private String productBaseName;

    @JsonProperty("org_name")
    private String orgName;

    @JsonProperty("comments")
    private String comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWishes() {
        return wishes;
    }

    public void setWishes(String wishes) {
        this.wishes = wishes;
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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

    public String getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setModifiedAccountId(String modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public String getProductBaseName() {
        return productBaseName;
    }

    public void setProductBaseName(String productBaseName) {
        this.productBaseName = productBaseName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Marketing() {

    }

    public Marketing(MarketingObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setName(object.getName());
            this.setWishes(object.getWishes());
            this.setOrgId(object.getOrgId());
            this.setProductBaseId(object.getProductBaseId());
            this.setTypeCode(object.getTypeCode());
            this.setBudget(object.getBudget());
            this.setBalance(object.getBalance());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedAccountId(object.getModifiedAccountId());
            this.setModifiedDateTime(object.getModifiedDateTime());
            this.setStatusCode(object.getStatusCode());
            this.setComments(object.getComments());
        }

    }

    public MarketingObject toMarketingObject() {
        MarketingObject object = new MarketingObject();
        object.setId(this.getId());
        object.setName(this.getName());
        object.setWishes(this.getWishes());
        object.setOrgId(this.getOrgId());
        object.setProductBaseId(this.getProductBaseId());
        object.setTypeCode(this.getTypeCode());
        object.setBudget(this.getBudget());
        object.setBalance(this.getBalance());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedAccountId(this.getModifiedAccountId());
        object.setModifiedDateTime(this.getModifiedDateTime());
        object.setStatusCode(this.getStatusCode());
        object.setComments(this.getComments());
        return object;
    }


}
