package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.UserObject;

/**
 * Created by:   Lijian
 * Created on:   2015/8/27
 * Descriptions:
 */
public class UserRegisterRequest {

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserObject toUserObject() {
        UserObject userObject = new UserObject();
        userObject.setDeviceId(deviceId);
        userObject.setPhone(phone);
        userObject.setName(name);
        userObject.setAddress(address);
        return userObject;
    }
}
