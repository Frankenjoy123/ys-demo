package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dake Wang on 2016/5/12.
 * TODO 定义地域报表结构
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
public class MarketUserLocationReport {

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
        private int count;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }


}





