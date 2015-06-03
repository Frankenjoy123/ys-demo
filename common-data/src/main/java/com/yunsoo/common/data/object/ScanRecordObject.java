package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Zhe on 2015/6/3.
 */
public class ScanRecordObject {
    @JsonProperty("id")
    private Long Id;
    @JsonProperty("product_key")
    private String productKey;
    @JsonProperty("base_product_id")
    private String baseProductId;
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("device_id")
    private String deviceId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("detail")
    private String detail;
    @JsonProperty("created_datetime")
    private String createdDateTime;
    @JsonProperty("location")
    private String location;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    @JsonIgnore
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(String baseProductId) {
        this.baseProductId = baseProductId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
