package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   yan
 * Created on:   4/27/2016
 * Descriptions:
 */
public class BrandApplicationHistoryObject extends BrandApplicationObject {

    @JsonProperty("history_id")
    private String historyId;


    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

}