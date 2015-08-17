package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
@Entity
@Table(name = "user_point")
public class UserPointEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "point")
    private int point;

    @Column(name = "last_sign_in_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastSignInDateTime;

    @Column(name = "continuous_sign_in_days")
    private int continuousSignInDays;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public DateTime getLastSignInDateTime() {
        return lastSignInDateTime;
    }

    public void setLastSignInDateTime(DateTime lastSignInDateTime) {
        this.lastSignInDateTime = lastSignInDateTime;
    }

    public int getContinuousSignInDays() {
        return continuousSignInDays;
    }

    public void setContinuousSignInDays(int continuousSignInDays) {
        this.continuousSignInDays = continuousSignInDays;
    }
}
