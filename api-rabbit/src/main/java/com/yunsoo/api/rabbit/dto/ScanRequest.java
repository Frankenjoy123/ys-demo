package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.UserScanRecordObject;

/**
 * Created by:   Zhe
 * Created on:   2015/3/24
 * Descriptions:
 */
public class ScanRequest {

    @JsonProperty("key")
    private String key;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("address")
    private String address;

    @JsonProperty("details")
    private String details;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public UserScanRecordObject toUserScanRecordObject() {
        UserScanRecordObject userScanRecordObject = new UserScanRecordObject();
        userScanRecordObject.setProductKey(this.key);
        userScanRecordObject.setLongitude(this.longitude);
        userScanRecordObject.setLatitude(this.latitude);
        userScanRecordObject.setProvince(this.province);
        userScanRecordObject.setCity(this.city);
        userScanRecordObject.setAddress(this.address);
        userScanRecordObject.setDeviceId(this.details);
        return userScanRecordObject;
    }
}
