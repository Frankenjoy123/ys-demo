package com.yunsoo.di.dao.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * Created by yqy09_000 on 2016/10/26.
 */
public class JobEntity {

    private String name;

    private String status;

    private int errors;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime jobStartDateTime;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime jobEndDateTime;

    private String errorMsg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public DateTime getJobStartDateTime() {
        return jobStartDateTime;
    }

    public void setJobStartDateTime(DateTime jobStartDateTime) {
        this.jobStartDateTime = jobStartDateTime;
    }

    public DateTime getJobEndDateTime() {
        return jobEndDateTime;
    }

    public void setJobEndDateTime(DateTime jobEndDateTime) {
        this.jobEndDateTime = jobEndDateTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
