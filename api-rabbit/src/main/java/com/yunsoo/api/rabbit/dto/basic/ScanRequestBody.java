package com.yunsoo.api.rabbit.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.web.exception.BadRequestException;

/**
 * Created by Zhe on 2015/3/24.
 */
public class ScanRequestBody {
    @JsonProperty("key")
    private String Key;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("device_code")
    private String deviceCode;
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    @JsonProperty("location")
    private String location;
    @JsonProperty("detail")
    private String detail;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Boolean validateForScan() {
        if (this.getKey() == null || this.getKey().isEmpty()) {
            throw new BadRequestException("Key不能为空！");
        }
        if (userId == null && (deviceCode == null || deviceCode.isEmpty())) {
            throw new BadRequestException("至少需要指定一个参数： user_id或device_code！");
        }
        return true;
    }
}
