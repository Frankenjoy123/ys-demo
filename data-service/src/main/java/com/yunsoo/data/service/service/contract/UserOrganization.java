package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.UserOrgEntity;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/15.
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

    public static UserOrgEntity ToEntity(UserOrganization userOrganization) {
        if (userOrganization == null) return null;

        UserOrgEntity entity = new UserOrgEntity();
        BeanUtils.copyProperties(userOrganization, entity, new String[]{"createdDateTime", "id"});
//        if (userLikedProduct.getId() != null && userLikedProduct.getId() != 0) {
//            entity.setId(userLikedProduct.getId());
//        }
        if (userOrganization.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(DateTime.parse(userOrganization.getCreatedDateTime()));
        }
        return entity;
    }

    public static UserOrganization FromEntity(UserOrgEntity entity) {
        if (entity == null) return null;

        UserOrganization userOrganization = new UserOrganization();
        BeanUtils.copyProperties(entity, userOrganization, new String[]{"createdDateTime"});
        if (entity.getCreatedDateTime() != null) {
            userOrganization.setCreatedDateTime(entity.getCreatedDateTime().toString());
        }
        return userOrganization;
    }

    public static List<UserOrganization> FromEntityList(Iterable<UserOrgEntity> entityList) {
        if (entityList == null) return null;
        List<UserOrganization> userOrganizationList = new ArrayList<UserOrganization>();
        for (UserOrgEntity entity : entityList) {
            userOrganizationList.add(UserOrganization.FromEntity(entity));
        }
        return userOrganizationList;
    }

    public static List<UserOrgEntity> ToEntityList(Iterable<UserOrganization> userOrganizations) {
        if (userOrganizations == null) return null;
        List<UserOrgEntity> entityList = new ArrayList<UserOrgEntity>();
        for (UserOrganization userOrganization : userOrganizations) {
            entityList.add(UserOrganization.ToEntity(userOrganization));
        }
        return entityList;
    }
}
