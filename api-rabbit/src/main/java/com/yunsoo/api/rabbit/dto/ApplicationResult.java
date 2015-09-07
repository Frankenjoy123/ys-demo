package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Zhe on 2015/6/15.
 */
public class ApplicationResult {
    @JsonProperty("current_app_version")
    private Application currentApplication;

    @JsonProperty("latest_app_version")
    private Application latestApplication;

    public Application getCurrentApplication() {
        return currentApplication;
    }

    public void setCurrentApplication(Application currentApplication) {
        this.currentApplication = currentApplication;
    }

    public Application getLatestApplication() {
        return latestApplication;
    }

    public void setLatestApplication(Application latestApplication) {
        this.latestApplication = latestApplication;
    }
}
