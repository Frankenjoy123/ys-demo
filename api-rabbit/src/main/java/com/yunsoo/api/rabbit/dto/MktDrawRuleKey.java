package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.MktDrawRuleKeyObject;

/**
 * Created by:  haitao
 * Created on:  2016/9/8
 * Descriptions:
 */
public class MktDrawRuleKey {
    @JsonProperty("id")
    private String id;

    @JsonProperty("marketing_id")
    private String marketingId;

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("rule_id")
    private String ruleId;

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

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public MktDrawRuleKey() {
    }

    public MktDrawRuleKey(MktDrawRuleKeyObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setMarketingId(object.getMarketingId());
            this.setProductKey(object.getProductKey());
            this.setRuleId(object.getRuleId());
        }
    }

    public MktDrawRuleKeyObject toMktDrawRuleKeyObject() {
        MktDrawRuleKeyObject object = new MktDrawRuleKeyObject();
        object.setId(this.getId());
        object.setMarketingId(this.getMarketingId());
        object.setProductKey(this.getProductKey());
        object.setRuleId(this.getRuleId());
        return object;
    }

}
