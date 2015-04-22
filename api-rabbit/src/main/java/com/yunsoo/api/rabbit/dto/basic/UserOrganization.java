package com.yunsoo.api.rabbit.dto.basic;

/**
 * Created by Zhe on 2015/4/21.
 */
public class UserOrganization {
    private long id;
    private String userId;
    private String organizationId;
    private long lastReadMessageId;
    private long isFollowing;
    private String createdDateTime;
    private String lastUpdatedDateTime;

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

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
        this.lastUpdatedDateTime = lastUpdatedDateTime;
    }
}