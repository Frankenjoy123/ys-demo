package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by admin on 2016/5/3.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MktDrawPrizeResult {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("used_number")
    private Integer usedNumber;


    @JsonProperty("total_number")
    private Integer totalNumber;


    @JsonProperty("available_number")
    private Integer availableNumber;

    @JsonProperty("available_amount")
    private double availableAmount;

    @JsonProperty("percentage_used")
    private Double percentageUsed;

    @JsonProperty("percentage_amount")
    private Double percentageAmount;

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

    public Integer getUsedNumber() {
        return usedNumber;
    }

    public void setUsedNumber(Integer usedNumber) {
        this.usedNumber = usedNumber;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getAvailableNumber() {
        return availableNumber;
    }

    public void setAvailableNumber(Integer availableNumber) {
        this.availableNumber = availableNumber;
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(double availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Double getPercentageUsed() {
        return percentageUsed;
    }

    public void setPercentageUsed(Double percentageUsed) {
        this.percentageUsed = percentageUsed;
    }

    public Double getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(Double percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public MktDrawPrizeResult() {
    }

}
