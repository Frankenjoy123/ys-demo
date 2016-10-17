package com.yunsoo.marketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public class MarketingRightRedeemCode {
    @JsonProperty("id")
    private String id;

    @JsonProperty("marketing_right_id")
    private String marketingRightId;

    @JsonProperty("value")
    private String value;

    @JsonProperty("used")
    private Boolean used;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarketingRightId() {
        return marketingRightId;
    }

    public void setMarketingRightId(String marketingRightId) {
        this.marketingRightId = marketingRightId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}
