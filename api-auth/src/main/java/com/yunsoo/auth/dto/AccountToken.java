package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */
public class AccountToken implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("permanent_token")
    private String permanentToken;

    @JsonProperty("permanent_token_expires_datetime")
    private DateTime permanentTokenExpiresDateTime;

    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getPermanentToken() {
        return permanentToken;
    }

    public void setPermanentToken(String permanentToken) {
        this.permanentToken = permanentToken;
    }

    public DateTime getPermanentTokenExpiresDateTime() {
        return permanentTokenExpiresDateTime;
    }

    public void setPermanentTokenExpiresDateTime(DateTime permanentTokenExpiresDateTime) {
        this.permanentTokenExpiresDateTime = permanentTokenExpiresDateTime;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
