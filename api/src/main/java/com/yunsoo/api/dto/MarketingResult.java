package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by admin on 2016/4/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketingResult {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("marketing_number")
    private Long marketingNumber;

    @JsonProperty("total_number")
    private Long totalNumber;

    @JsonProperty("rule_list")
    private List<MktDrawRule> ruleList;

    @JsonProperty("prize_count_list")
    private List<Long> prizeCountList;

    @JsonProperty("prize_result_list")
    private List<MktDrawPrizeResult> prizeResultList;


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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Long getMarketingNumber() {
        return marketingNumber;
    }

    public void setMarketingNumber(Long marketingNumber) {
        this.marketingNumber = marketingNumber;
    }

    public Long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<MktDrawRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<MktDrawRule> ruleList) {
        this.ruleList = ruleList;
    }

    public List<Long> getPrizeCountList() {
        return prizeCountList;
    }

    public void setPrizeCountList(List<Long> prizeCountList) {
        this.prizeCountList = prizeCountList;
    }

    public List<MktDrawPrizeResult> getPrizeResultList() {
        return prizeResultList;
    }

    public void setPrizeResultList(List<MktDrawPrizeResult> prizeResultList) {
        this.prizeResultList = prizeResultList;
    }

    public MarketingResult() {
    }

}
