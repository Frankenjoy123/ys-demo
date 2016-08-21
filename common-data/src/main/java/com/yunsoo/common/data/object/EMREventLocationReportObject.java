package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/26
 * Descriptions: location report: Scan, Share, Store_url, Comment Count for event and user
 */
public class EMREventLocationReportObject implements Serializable {

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("event_count")
    private EMREventCountObject event_count;

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
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

    public EMREventCountObject getEvent_count() {
        return event_count;
    }

    public void setEvent_count(EMREventCountObject event_count) {
        this.event_count = event_count;
    }

    public final static String ALL_PROVINCE = "all";
    public final static String ALL_CITY = "all";
}
