package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.EMRUserReportObject;

import java.util.List;

public class EMRUserReport {

    // scan, wx, draw, win, reward
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

    public EMRUserReport() {
    }

    public EMRUserReport(EMRUserReportObject object) {
        if (object != null) {
            this.setEventCount(object.getEventCount());
            this.setUserCount(object.getUserCount());
        }
    }
}
