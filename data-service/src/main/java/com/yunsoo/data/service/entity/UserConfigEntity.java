package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by:   Lijian
 * Created on:   2015/9/16
 * Descriptions:
 */
@Entity
@Table(name = "user_config")
public class UserConfigEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "auto_following")
    private boolean autoFollowing;

    @Column(name = "modified_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedDateTime;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAutoFollowing() {
        return autoFollowing;
    }

    public void setAutoFollowing(boolean autoFollowing) {
        this.autoFollowing = autoFollowing;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }
}
