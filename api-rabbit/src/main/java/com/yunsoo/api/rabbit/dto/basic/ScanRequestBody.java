package com.yunsoo.api.rabbit.dto.basic;

import com.yunsoo.common.web.exception.BadRequestException;

/**
 * Created by Zhe on 2015/3/24.
 */
public class ScanRequestBody {

    private String Key;
    private Integer userId;
    private String deviceCode;
    private Double latitude;
    private Double longitude;
    private String location;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
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

    public Boolean validateForScan() {
        if (this.getKey() == null || this.getKey().isEmpty()) {
            throw new BadRequestException("Key不能为空！");
        }
        if (userId == null && (deviceCode == null || deviceCode.isEmpty())) {
            throw new BadRequestException("至少需要指定一个参数： userId或deviceCode！");
        }
        return true;
    }
}
