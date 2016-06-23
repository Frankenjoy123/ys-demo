package com.yunsoo.processor.dao.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@Entity
@Table(name = "processor_task")
public class TaskEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "executor")
    private String executor;

    @Column(name = "environment")
    private String environment;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "\"interval\"")
    private String interval;

    @Column(name = "next_run_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime nextRunDateTime;

    @Column(name = "parameters")
    private String parameters;

    @Column(name = "locked_by")
    private String lockedBy;

    @Column(name = "start_run_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startRunDateTime;

    @Column(name = "timeout_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime timeoutDateTime;

    @Column(name = "last_run_by")
    private String lastRunBy;

    @Column(name = "last_finished_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastFinishedDateTime;

    @Column(name = "failed_count")
    private Integer failedCount;

    @Column(name = "last_failed_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastFailedDateTime;

    @Column(name = "last_failed_reason")
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

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
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

    public DateTime getNextRunDateTime() {
        return nextRunDateTime;
    }

    public void setNextRunDateTime(DateTime nextRunDateTime) {
        this.nextRunDateTime = nextRunDateTime;
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

    public DateTime getStartRunDateTime() {
        return startRunDateTime;
    }

    public void setStartRunDateTime(DateTime startRunDateTime) {
        this.startRunDateTime = startRunDateTime;
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