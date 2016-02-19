package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dake Wang on 2016/2/4.
 * <p>
 * <p>
 * {
 * *
 * <p>
 * data:[
 * {name:xxx, value:12, city[]},...
 * }]
 * <p>
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanLocationAnalysisReport {

    @JsonProperty("data")
    private NameValue[] provinceData;


    public NameValue[] getProvinceData() {
        return provinceData;
    }

    public void setProvinceData(NameValue[] provinceData) {
        this.provinceData = provinceData;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public final static class NameValue {
        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private int value;

        @JsonProperty("city")
        private NameValue[] cityData;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public NameValue[] getCityData() {
            return cityData;
        }

        public void setCityData(NameValue[] cityData) {
            this.cityData = cityData;
        }
    }


}





