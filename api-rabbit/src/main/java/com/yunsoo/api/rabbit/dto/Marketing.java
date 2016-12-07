package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2015/1/25
 * Descriptions:
 */
public class Marketing {

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

    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("prize_type_code")
    private String prizeTypeCode;

    @JsonProperty("product_base_name")
    private String productBaseName;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("start_datetime")
    private DateTime startDateTime;


    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("end_datetime")
    private DateTime endDateTime;

    @JsonProperty("rules_text")
    private String rulesText;

    @JsonProperty("is_prized_all")
    private Boolean isPrizedAll;

    @JsonProperty("is_mobile_verified")
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

    public String getPrizeTypeCode() {
        return prizeTypeCode;
    }

    public void setPrizeTypeCode(String prizeTypeCode) {
        this.prizeTypeCode = prizeTypeCode;
    }

    public String getProductBaseName() {
        return productBaseName;
    }

    public void setProductBaseName(String productBaseName) {
        this.productBaseName = productBaseName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
            this.setOrgId(object.getOrgId());
            this.setProductBaseId(object.getProductBaseId());
            this.setTypeCode(object.getTypeCode());
            this.setBudget(object.getBudget());
            this.setPrizeTypeCode(object.getPrizeTypeCode());
            this.setQuantity(object.getQuantity());
            this.setStartDateTime(object.getStartDateTime());
            this.setEndDateTime(object.getEndDateTime());
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
        object.setOrgId(this.getOrgId());
        object.setProductBaseId(this.getProductBaseId());
        object.setTypeCode(this.getTypeCode());
        object.setBudget(this.getBudget());
        object.setPrizeTypeCode(this.getPrizeTypeCode());
        object.setQuantity(this.getQuantity());
        object.setStartDateTime(this.getStartDateTime());
        object.setEndDateTime(this.getEndDateTime());
        object.setRulesText(this.getRulesText());
        object.setIsPrizedAll(this.getIsPrizedAll());
        object.setIsMobileVerified(this.getIsMobileVerified());
        return object;
    }


}
