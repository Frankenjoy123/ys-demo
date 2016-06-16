package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yqy09_000 on 6/16/2016.
 */
public class PeriodUserConsumptionStatsObject {

    @JsonProperty("total")
    private int totalCount;

    @JsonProperty("weekly")
    private double weeklyCount;

    @JsonProperty("daily")
    private double dailyCount;

    @JsonProperty("monthly")
    private double monthlyCount;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getWeeklyCount() {
        return weeklyCount;
    }

    public void setWeeklyCount(double weeklyCount) {
        this.weeklyCount = weeklyCount;
    }

    public double getDailyCount() {
        return dailyCount;
    }

    public void setDailyCount(double dailyCount) {
        this.dailyCount = dailyCount;
    }

    public double getMonthlyCount() {
        return monthlyCount;
    }

    public void setMonthlyCount(double monthlyCount) {
        this.monthlyCount = monthlyCount;
    }
}
