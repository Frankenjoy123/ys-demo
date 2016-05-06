package com.yunsoo.common.data.object.processor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
public class TaskObject {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("executor")
    private String executor;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("interval")
    private String interval;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("next_run_datetime")
    private DateTime nextRunDatetime;

    @JsonProperty("parameters")
    private String parameters;

    @JsonProperty("locked_by")
    private String lockedBy;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("start_run_datetime")
    private DateTime startRunDatetime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("timeout_datetime")
    private DateTime timeoutDateTime;

    @JsonProperty("last_run_by")
    private String lastRunBy;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("last_finished_datetime")
    private DateTime lastFinishedDateTime;

    @JsonProperty("failed_count")
    private Integer failedCount;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("last_failed_datetime")
    private DateTime lastFailedDateTime;

    @JsonProperty("last_failed_reason")
    private String lastFailedReason;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public DateTime getNextRunDatetime() {
        return nextRunDatetime;
    }

    public void setNextRunDatetime(DateTime nextRunDatetime) {
        this.nextRunDatetime = nextRunDatetime;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public DateTime getStartRunDatetime() {
        return startRunDatetime;
    }

    public void setStartRunDatetime(DateTime startRunDatetime) {
        this.startRunDatetime = startRunDatetime;
    }

    public DateTime getTimeoutDateTime() {
        return timeoutDateTime;
    }

    public void setTimeoutDateTime(DateTime timeoutDateTime) {
        this.timeoutDateTime = timeoutDateTime;
    }

    public String getLastRunBy() {
        return lastRunBy;
    }

    public void setLastRunBy(String lastRunBy) {
        this.lastRunBy = lastRunBy;
    }

    public DateTime getLastFinishedDateTime() {
        return lastFinishedDateTime;
    }

    public void setLastFinishedDateTime(DateTime lastFinishedDateTime) {
        this.lastFinishedDateTime = lastFinishedDateTime;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public DateTime getLastFailedDateTime() {
        return lastFailedDateTime;
    }

    public void setLastFailedDateTime(DateTime lastFailedDateTime) {
        this.lastFailedDateTime = lastFailedDateTime;
    }

    public String getLastFailedReason() {
        return lastFailedReason;
    }

    public void setLastFailedReason(String lastFailedReason) {
        this.lastFailedReason = lastFailedReason;
    }
}
