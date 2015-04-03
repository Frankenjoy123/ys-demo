package com.yunsoo.api.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 这个对象对应dataapi所用的数据接口。
 * Created by Zhe on 2015/3/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TScanRecord {
    private String productKey;
    private long baseProductId;
    private int clientId;
    private String deviceId;
    private Long userId;
    private String detail;
    private String createdDateTime;
    private Double latitude;
    private Double longitude;
    private String location;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public long getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(long baseProductId) {
        this.baseProductId = baseProductId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
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
}
