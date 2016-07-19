package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.EMRActionCountObject;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/19
 * Descriptions: Share, Store_url, Comment report
 */
public class EMRActionCount {
    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("last_day_count")
    private int lastDayCount;

    @JsonProperty("last_week_count")
    private int lastWeekCount;

    @JsonProperty("last_month_count")
    private int lastMonthCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getLastDayCount() {
        return lastDayCount;
    }

    public void setLastDayCount(int lastDayCount) {
        this.lastDayCount = lastDayCount;
    }

    public int getLastWeekCount() {
        return lastWeekCount;
    }

    public void setLastWeekCount(int lastWeekCount) {
        this.lastWeekCount = lastWeekCount;
    }

    public int getLastMonthCount() {
        return lastMonthCount;
    }

    public void setLastMonthCount(int lastMonthCount) {
        this.lastMonthCount = lastMonthCount;
    }

    public EMRActionCount() {
    }

    public EMRActionCount(EMRActionCountObject emrActionCountObject) {
        this.setTotalCount(emrActionCountObject.getTotalCount());
        this.setLastDayCount(emrActionCountObject.getLastDayCount());
        this.setLastWeekCount(emrActionCountObject.getLastWeekCount());
        this.setLastMonthCount(emrActionCountObject.getLastMonthCount());
    }

}
