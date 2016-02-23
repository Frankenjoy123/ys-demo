package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.MktDrawRuleObject;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2015/1/25
 * Descriptions:
 */
public class MktDrawRule {

    @JsonProperty("id")
    private String id;

    @JsonProperty("marketing_id")
    private String marketingId;

    @JsonProperty("prize_type_code")
    private String prizeTypeCode;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("probability")
    private double probability;

    @JsonProperty("comments")
    private String comments;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public String getPrizeTypeCode() {
        return prizeTypeCode;
    }

    public void setPrizeTypeCode(String prizeTypeCode) {
        this.prizeTypeCode = prizeTypeCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public MktDrawRule() {
    }

    public MktDrawRule(MktDrawRuleObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setMarketingId(object.getMarketingId());
            this.setPrizeTypeCode(object.getPrizeTypeCode());
            this.setAmount(object.getAmount());
            this.setProbability(object.getProbability());
            this.setComments(object.getComments());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedAccountId(object.getModifiedAccountId());
            this.setModifiedDateTime(object.getModifiedDateTime());

        }
    }

    public MktDrawRuleObject toMktDrawRuleObject() {
        MktDrawRuleObject object = new MktDrawRuleObject();
        object.setId(this.getId());
        object.setMarketingId(this.getMarketingId());
        object.setPrizeTypeCode(this.getPrizeTypeCode());
        object.setAmount(this.getAmount());
        object.setProbability(this.getProbability());
        object.setComments(this.getComments());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedAccountId(this.getModifiedAccountId());
        object.setModifiedDateTime(this.getModifiedDateTime());
        return object;
    }
}
