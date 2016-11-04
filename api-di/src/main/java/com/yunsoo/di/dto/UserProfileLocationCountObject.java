package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yqy09_000 on 2016/8/26.
 */
public class UserProfileLocationCountObject {

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("count")
    private int count;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
