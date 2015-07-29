package com.yunsoo.common.data.object;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/20
 * Descriptions:
 */
public class AccountTokenObject implements Serializable {

    private String id;
    private String accountId;
    private String appId;
    private String deviceId;
    private String permanentToken;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime permanentTokenExpiresDateTime;
    private String createdAccountId;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
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

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
