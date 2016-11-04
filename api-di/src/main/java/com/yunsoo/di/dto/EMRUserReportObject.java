package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by yqy09_000 on 6/1/2016.
 */
public class EMRUserReportObject {

    // scan, draw, win, reward
    @JsonProperty("event_count")
    private List<Integer> eventCount;
    @JsonProperty("user_count")
    private List<Integer> userCount;

    public List<Integer> getEventCount() {
        return eventCount;
    }

    public void setEventCount(List<Integer> eventCount) {
        this.eventCount = eventCount;
    }

    public List<Integer> getUserCount() {
        return userCount;
    }

    public void setUserCount(List<Integer> userCount) {
        this.userCount = userCount;
    }
}
