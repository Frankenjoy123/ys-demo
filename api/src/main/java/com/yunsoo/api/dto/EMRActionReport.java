package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.EMRActionReportObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/19
 * Descriptions: Share, Store_url, Comment report
 */
public class EMRActionReport {
    // share, store_url, comment
    @JsonProperty("event_count")
    private List<EMRActionCount> eventCount;

    @JsonProperty("user_count")
    private List<EMRActionCount> userCount;

    public List<EMRActionCount> getEventCount() {
        return eventCount;
    }

    public void setEventCount(List<EMRActionCount> eventCount) {
        this.eventCount = eventCount;
    }

    public List<EMRActionCount> getUserCount() {
        return userCount;
    }

    public void setUserCount(List<EMRActionCount> userCount) {
        this.userCount = userCount;
    }

    public EMRActionReport(EMRActionReportObject object) {
        if (object != null) {
            this.setEventCount(object.getEventCount().stream().map(EMRActionCount::new).collect(Collectors.toList()));
            this.setUserCount(object.getEventCount().stream().map(EMRActionCount::new).collect(Collectors.toList()));
        }
    }

}
