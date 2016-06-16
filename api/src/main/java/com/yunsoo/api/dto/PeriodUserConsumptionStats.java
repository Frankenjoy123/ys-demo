package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.PeriodUserConsumptionStatsObject;

/**
 * Created by Admin on 6/16/2016.
 */
public class PeriodUserConsumptionStats {

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

    public PeriodUserConsumptionStats() {
    }

    public PeriodUserConsumptionStats(PeriodUserConsumptionStatsObject object) {
        if (object != null) {
            this.setDailyCount(object.getDailyCount());
            this.setMonthlyCount(object.getMonthlyCount());
            this.setTotalCount(object.getTotalCount());
            this.setWeeklyCount(object.getWeeklyCount());

        }
    }
}
