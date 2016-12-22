package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.MktSellerObject;

/**
 * Created by:  haitao
 * Created on:  2016/12/6
 * Descriptions:
 */
public class MktSeller {
    @JsonProperty("openid")
    private String openid;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sex")
    private Boolean sex;

    @JsonProperty("city")
    private String city;

    @JsonProperty("province")
    private String province;

    @JsonProperty("country")
    private String country;

    @JsonProperty("gravatar_url")
    private String gravatarUrl;

    @JsonProperty("shop_url")
    private String shopUrl;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public MktSeller() {
    }

    public MktSeller(MktSellerObject object) {
        if (object != null) {
            this.setOpenid(object.getOpenid());
            this.setOrgId(object.getOrgId());
            this.setName(object.getName());
            this.setSex(object.getSex());
            this.setCity(object.getCity());
            this.setProvince(object.getProvince());
            this.setCountry(object.getCountry());
            this.setGravatarUrl(object.getGravatarUrl());
            this.setShopUrl(object.getShopUrl());
        }
    }

    public MktSellerObject toMktSellerObject() {
        MktSellerObject object = new MktSellerObject();
        object.setOpenid(this.getOpenid());
        object.setOrgId(this.getOrgId());
        object.setName(this.getName());
        object.setSex(this.getSex());
        object.setCity(this.getCity());
        object.setProvince(this.getProvince());
        object.setCountry(this.getCountry());
        object.setGravatarUrl(this.getGravatarUrl());
        object.setShopUrl(this.getShopUrl());
        return object;
    }

}
