package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by:   Lijian
 * Created on:   2015/8/27
 * Descriptions:
 */
public class UserLoginRequest {

    @JsonProperty("device_id")
    private String deviceId;

    @NotEmpty(message = "phone must not be null or empty")
    @JsonProperty("phone")
    private String phone;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
