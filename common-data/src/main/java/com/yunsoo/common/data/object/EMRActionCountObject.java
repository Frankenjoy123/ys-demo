package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Haitao
 * Created on  : 2016/7/19
 * Descriptions: Share, Store_url, Comment report
 */
public class EMRActionCountObject {

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
}
