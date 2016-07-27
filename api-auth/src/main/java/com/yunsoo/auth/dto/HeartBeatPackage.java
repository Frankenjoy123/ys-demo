package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.auth.Constants;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-07-25
 * Descriptions:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HeartBeatPackage implements Serializable {

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("status")
    private String status;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("last_beat_datetime")
    private DateTime lastBeatDateTime;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getLastBeatDateTime() {
        return lastBeatDateTime;
    }

    public void setLastBeatDateTime(DateTime lastBeatDateTime) {
        this.lastBeatDateTime = lastBeatDateTime;
    }

    public static HeartBeatPackage offline(String deviceId) {
        HeartBeatPackage pkg = new HeartBeatPackage();
        pkg.setDeviceId(deviceId);
        pkg.setStatus(Constants.DeviceStatus.OFFLINE);
        return pkg;
    }
}
