package com.yunsoo.api.security;

/**
 * Created by:   Lijian
 * Created on:   2016-07-26
 * Descriptions:
 */
public class AuthDetails {

    private String appId;

    private String deviceId;


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
}
