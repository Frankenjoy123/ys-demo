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
@Table(name = "user_activity")
public class UserActivityEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "last_sign_in_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastSignInDateTime;

    @Column(name = "last_sign_in_continuous_days")
    private Integer lastSignInContinuousDays;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DateTime getLastSignInDateTime() {
        return lastSignInDateTime;
    }

    public void setLastSignInDateTime(DateTime lastSignInDateTime) {
        this.lastSignInDateTime = lastSignInDateTime;
    }

    public Integer getLastSignInContinuousDays() {
        return lastSignInContinuousDays;
    }

    public void setLastSignInContinuousDays(Integer lastSignInContinuousDays) {
        this.lastSignInContinuousDays = lastSignInContinuousDays;
    }
}
