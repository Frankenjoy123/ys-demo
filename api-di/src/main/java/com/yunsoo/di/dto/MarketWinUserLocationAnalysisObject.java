package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by:   Dake
 * Created on:   2016/5/11
 * Descriptions:
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MarketWinUserLocationAnalysisObject implements Serializable {

    @JsonProperty("value")
    private int value;

    @JsonProperty("name")
    private String name;

    @JsonProperty("city")
    private List<MarketWinUserLocationAnalysisObject> cityData;;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MarketWinUserLocationAnalysisObject> getCityData() {
        return cityData;
    }

    public void setCityData(List<MarketWinUserLocationAnalysisObject> cityData) {
        this.cityData = cityData;
    }
}
