package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.api.dto.detectable.OrgIdDetectable;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

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

    @JsonProperty("quantity")
    private Integer quantity;

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

    @JsonProperty("prize_type_code")
    private String prizeTypeCode;

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

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("start_datetime")
    private DateTime startDateTime;


    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("end_datetime")
    private DateTime endDateTime;


    @JsonProperty("product_base_name")
    private String productBaseName;

    @JsonProperty("org_name")
    private String orgName;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("rules_text")
    private String rulesText;

    @JsonProperty("marketing_rules")
    private List<MktDrawRule> marketingRules;

    @JsonProperty("isPrizedAll")
    private Boolean isPrizedAll;

    @JsonProperty("isMobileVerified")
    private Boolean isMobileVerified;


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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public String getPrizeTypeCode() {
        return prizeTypeCode;
    }

    public void setPrizeTypeCode(String prizeTypeCode) {
        this.prizeTypeCode = prizeTypeCode;
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

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
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

    public List<MktDrawRule> getMarketingRules() {
        return marketingRules;
    }

    public void setMarketingRules(List<MktDrawRule> marketingRules) {
        this.marketingRules = marketingRules;
    }

    public String getRulesText() {
        return rulesText;
    }

    public void setRulesText(String rulesText) {
        this.rulesText = rulesText;
    }

    public Boolean getIsPrizedAll() {
        return isPrizedAll;
    }

    public void setIsPrizedAll(Boolean isPrizedAll) {
        this.isPrizedAll = isPrizedAll;
    }

    public Boolean getIsMobileVerified() {
        return isMobileVerified;
    }

    public void setIsMobileVerified(Boolean isMobileVerified) {
        this.isMobileVerified = isMobileVerified;
    }

    public Marketing() {

    }

    public Marketing(MarketingObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setName(object.getName());
            this.setWishes(object.getWishes());
            this.setQuantity(object.getQuantity());
            this.setOrgId(object.getOrgId());
            this.setProductBaseId(object.getProductBaseId());
            this.setTypeCode(object.getTypeCode());
            this.setBudget(object.getBudget());
            this.setBalance(object.getBalance());
            this.setPrizeTypeCode(object.getPrizeTypeCode());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedAccountId(object.getModifiedAccountId());
            this.setModifiedDateTime(object.getModifiedDateTime());
            this.setStartDateTime(object.getStartDateTime());
            this.setEndDateTime(object.getEndDateTime());
            this.setStatusCode(object.getStatusCode());
            this.setComments(object.getComments());
            this.setRulesText(object.getRulesText());
            this.setIsPrizedAll(object.getIsPrizedAll());
            this.setIsMobileVerified(object.getIsMobileVerified());
        }

    }

    public MarketingObject toMarketingObject() {
        MarketingObject object = new MarketingObject();
        object.setId(this.getId());
        object.setName(this.getName());
        object.setWishes(this.getWishes());
        object.setQuantity(this.getQuantity());
        object.setOrgId(this.getOrgId());
        object.setProductBaseId(this.getProductBaseId());
        object.setTypeCode(this.getTypeCode());
        object.setBudget(this.getBudget());
        object.setBalance(this.getBalance());
        object.setPrizeTypeCode(this.getPrizeTypeCode());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedAccountId(this.getModifiedAccountId());
        object.setModifiedDateTime(this.getModifiedDateTime());
        object.setStartDateTime(this.getStartDateTime());
        object.setEndDateTime(this.getEndDateTime());
        object.setStatusCode(this.getStatusCode());
        object.setComments(this.getComments());
        object.setRulesText(this.getRulesText());
        object.setIsPrizedAll(this.getIsPrizedAll());
        object.setIsMobileVerified(this.getIsMobileVerified());
        return object;
    }


}
