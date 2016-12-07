package com.yunsoo.api.rabbit.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 12/7/2016.
 */
public class JuheMobileLocation {

    private String province;
    private String city;
    @JsonProperty("areacode")
    private String areaCode;
    private String zip;
    private String company;
    private String card;

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

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
