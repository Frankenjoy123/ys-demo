package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.MarketWinUserLocationAnalysisObject;

import java.util.ArrayList;
import java.util.List;

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

    @JsonProperty("city")
    private List<MarketWinUserLocationAnalysis> city;

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
            List<MarketWinUserLocationAnalysis> cityData = new ArrayList<MarketWinUserLocationAnalysis>();
            if (object.getCityData().size() > 0) {
                for (MarketWinUserLocationAnalysisObject temp : object.getCityData()) {
                    int cityCount = temp.getValue();
                    String cityName = temp.getName();
                    MarketWinUserLocationAnalysis tempList = new MarketWinUserLocationAnalysis();
                    tempList.setProvince(cityName);
                    tempList.setCount(cityCount);
                    cityData.add(tempList);
                }
            }
            this.setCity(cityData);
        }


    }

    public List<MarketWinUserLocationAnalysis> getCity() {
        return city;
    }

    public void setCity(List<MarketWinUserLocationAnalysis> city) {
        this.city = city;
    }
}
