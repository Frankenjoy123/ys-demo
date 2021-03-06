package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/19
 * Descriptions: Share, Store_url, Comment report
 */
public class EMRActionReportObject implements Serializable {

    @JsonProperty("scan_event_count")
    private int scanEventCount;

    @JsonProperty("scan_user_count")
    private int scanUserCount;

    // share, store_url, comment
    @JsonProperty("event_count")
    private List<EMRActionCountObject> eventCount;

    @JsonProperty("user_count")
    private List<EMRActionCountObject> userCount;

    public int getScanEventCount() {
        return scanEventCount;
    }

    public void setScanEventCount(int scanEventCount) {
        this.scanEventCount = scanEventCount;
    }

    public int getScanUserCount() {
        return scanUserCount;
    }

    public void setScanUserCount(int scanUserCount) {
        this.scanUserCount = scanUserCount;
    }

    public List<EMRActionCountObject> getEventCount() {
        return eventCount;
    }

    public void setEventCount(List<EMRActionCountObject> eventCount) {
        this.eventCount = eventCount;
    }

    public List<EMRActionCountObject> getUserCount() {
        return userCount;
    }

    public void setUserCount(List<EMRActionCountObject> userCount) {
        this.userCount = userCount;
    }
}
