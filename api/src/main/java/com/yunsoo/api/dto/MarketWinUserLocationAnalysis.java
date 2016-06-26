package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.MarketWinUserLocationAnalysisObject;

/**
 * Created by:  haitao
 * Created on:  2016/6/1
 * Descriptions:
 */
public class MarketWinUserLocationAnalysis {
    @JsonProperty("count")
    private int count;

    @JsonProperty("province")
    private String province;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public MarketWinUserLocationAnalysis() {
    }

    public MarketWinUserLocationAnalysis(MarketWinUserLocationAnalysisObject object) {
        if (object != null) {
            this.setCount(object.getValue());
            this.setProvince(object.getName());
        }
    }

}
