package com.yunsoo.di.dao.entity;

import org.joda.time.LocalDate;

/**
 * Created by yqy09_000 on 2016/10/26.
 */
public class DailyJobReportEntity {
    private LocalDate date;
    private String jobName;
    private int succeedCount;
    private int failedCount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getSucceedCount() {
        return succeedCount;
    }

    public void setSucceedCount(int succeedCount) {
        this.succeedCount = succeedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }
}
