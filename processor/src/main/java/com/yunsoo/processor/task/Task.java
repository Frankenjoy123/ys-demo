package com.yunsoo.processor.task;

import com.yunsoo.processor.dao.entity.TaskEntity;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
public class Task {

    private String code;

    private String name;

    private String description;

    private String executor;

    private boolean enabled;

    private String interval;

    private DateTime nextRunDatetime;

    private String parameters;

    private String lockedBy;

    private DateTime startRunDatetime;

    private DateTime timeoutDateTime;

    private String lastRunBy;

    private DateTime lastFinishedDateTime;

    private int failedCount;

    private DateTime lastFailedDateTime;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
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

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
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

    public Task() {
    }

    public Task(TaskEntity entity) {
        if (entity != null) {
            this.setCode(entity.getCode());
            this.setName(entity.getName());
            this.setDescription(entity.getDescription());
            this.setExecutor(entity.getExecutor());
            this.setEnabled(entity.isEnabled());
            this.setInterval(entity.getInterval());
            this.setNextRunDatetime(entity.getNextRunDatetime());
            this.setParameters(entity.getParameters());
            this.setLockedBy(entity.getLockedBy());
            this.setStartRunDatetime(entity.getStartRunDatetime());
            this.setTimeoutDateTime(entity.getTimeoutDateTime());
            this.setLastRunBy(entity.getLastRunBy());
            this.setLastFinishedDateTime(entity.getLastFinishedDateTime());
            this.setFailedCount(entity.getFailedCount() == null ? 0 : entity.getFailedCount());
            this.setLastFailedDateTime(entity.getLastFailedDateTime());
            this.setLastFailedReason(entity.getLastFailedReason());
        }
    }

    public TaskEntity toTaskEntity() {
        TaskEntity entity = new TaskEntity();
        entity.setCode(this.getCode());
        entity.setName(this.getName());
        entity.setDescription(this.getDescription());
        entity.setExecutor(this.getExecutor());
        entity.setEnabled(this.isEnabled());
        entity.setInterval(this.getInterval());
        entity.setNextRunDatetime(this.getNextRunDatetime());
        entity.setParameters(this.getParameters());
        entity.setLockedBy(this.getLockedBy());
        entity.setStartRunDatetime(this.getStartRunDatetime());
        entity.setTimeoutDateTime(this.getTimeoutDateTime());
        entity.setLastRunBy(this.getLastRunBy());
        entity.setLastFinishedDateTime(this.getLastFinishedDateTime());
        entity.setFailedCount(this.getFailedCount());
        entity.setLastFailedDateTime(this.getLastFailedDateTime());
        entity.setLastFailedReason(this.getLastFailedReason());
        return entity;
    }
}
