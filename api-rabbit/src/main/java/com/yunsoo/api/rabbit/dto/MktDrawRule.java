package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.MktDrawRuleObject;

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

    @JsonProperty("consumer_right_id")
    private String consumerRightId;

    @JsonProperty("prize_type_code")
    private String prizeTypeCode;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("probability")
    private double probability;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("applied_env")
    private String appliedEnv;

    @JsonProperty("is_equal")
    private Boolean isEqual;


    @JsonProperty("mkt_consumer_right")
    private MktConsumerRight mktConsumerRight;

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

    public String getConsumerRightId() {
        return consumerRightId;
    }

    public void setConsumerRightId(String consumerRightId) {
        this.consumerRightId = consumerRightId;
    }

    public String getPrizeTypeCode() {
        return prizeTypeCode;
    }

    public void setPrizeTypeCode(String prizeTypeCode) {
        this.prizeTypeCode = prizeTypeCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    public String getAppliedEnv() {
        return appliedEnv;
    }

    public void setAppliedEnv(String appliedEnv) {
        this.appliedEnv = appliedEnv;
    }

    public Boolean getIsEqual() {
        return isEqual;
    }

    public void setIsEqual(Boolean isEqual) {
        this.isEqual = isEqual;
    }


    public MktConsumerRight getMktConsumerRight() {
        return mktConsumerRight;
    }

    public void setMktConsumerRight(MktConsumerRight mktConsumerRight) {
        this.mktConsumerRight = mktConsumerRight;
    }

    public MktDrawRule() {
    }

    public MktDrawRule(MktDrawRuleObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setMarketingId(object.getMarketingId());
            this.setConsumerRightId(object.getConsumerRightId());
            this.setPrizeTypeCode(object.getPrizeTypeCode());
            this.setAmount(object.getAmount());
            this.setProbability(object.getProbability());
            this.setComments(object.getComments());
            this.setAppliedEnv(object.getAppliedEnv());
            this.setIsEqual(object.getIsEqual());
        }
    }

    public MktDrawRuleObject toMktDrawRuleObject() {
        MktDrawRuleObject object = new MktDrawRuleObject();
        object.setId(this.getId());
        object.setMarketingId(this.getMarketingId());
        object.setConsumerRightId(this.getConsumerRightId());
        object.setPrizeTypeCode(this.getPrizeTypeCode());
        object.setAmount(this.getAmount());
        object.setProbability(this.getProbability());
        object.setComments(this.getComments());
        object.setAppliedEnv(this.getAppliedEnv());
        object.setIsEqual(this.getIsEqual());
        return object;
    }
}
