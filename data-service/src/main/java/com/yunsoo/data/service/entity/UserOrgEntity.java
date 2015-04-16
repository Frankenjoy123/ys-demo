package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Zhe on 2015/4/15.
 */
@Entity
@Table(name = "user_organization")
public class UserOrgEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "organization_id")
    private String organizationId;

    @Column(name = "last_read_message_id")
    private long lastReadMessageId;

    @Column(name = "is_following")
    private long isFollowing;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "last_update_time", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastUpdatedDateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public long getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

    public long getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(long isFollowing) {
        this.isFollowing = isFollowing;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(DateTime lastUpdatedDateTime) {
        this.lastUpdatedDateTime = lastUpdatedDateTime;
    }
}
