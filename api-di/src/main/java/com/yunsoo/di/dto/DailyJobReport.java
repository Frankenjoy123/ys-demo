package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yqy09_000 on 2016/10/26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyJobReport {

    @JsonProperty("date")
    private String date;
    @JsonProperty("job_name")
    private String jobName;
    @JsonProperty("succeed_count")
    private int succeedCount;
    @JsonProperty("failed_count")
    private int failedCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
