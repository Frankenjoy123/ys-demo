package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/22
 * Descriptions: daily count for Scan, Share, Store_url, Comment Count for event and user
 */
public class EMREventReportObject {

    @JsonProperty("event_count")
    private Map<String, EMREventCountObject> event_count;

    public Map<String, EMREventCountObject> getEvent_count() {
        return event_count;
    }

    public void setEvent_count(Map<String, EMREventCountObject> event_count) {
        this.event_count = event_count;
    }
}
