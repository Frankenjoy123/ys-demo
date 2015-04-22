package com.yunsoo.data.service.service.contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.data.service.entity.UserFollowingEntity;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/15.
 */
public class UserFollowing {

    private long id;
    private String userId;
    private String organizationId;
    private long lastReadMessageId;
    private Boolean isFollowing;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
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

    public Boolean getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Boolean isFollowing) {
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

    public static UserFollowingEntity ToEntity(UserFollowing userFollowing) {
        if (userFollowing == null) return null;

        UserFollowingEntity entity = new UserFollowingEntity();
        BeanUtils.copyProperties(userFollowing, entity, new String[]{"createdDateTime", "id"});
//        if (userLikedProduct.getId() != null && userLikedProduct.getId() != 0) {
//            entity.setId(userLikedProduct.getId());
//        }
        if (userFollowing.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(userFollowing.getCreatedDateTime());
        }
        return entity;
    }

    public static UserFollowing FromEntity(UserFollowingEntity entity) {
        if (entity == null) return null;

        UserFollowing userFollowing = new UserFollowing();
        BeanUtils.copyProperties(entity, userFollowing, new String[]{"createdDateTime"});
        if (entity.getCreatedDateTime() != null) {
            userFollowing.setCreatedDateTime(entity.getCreatedDateTime());
        }
        return userFollowing;
    }

    public static List<UserFollowing> FromEntityList(Iterable<UserFollowingEntity> entityList) {
        if (entityList == null) return null;
        List<UserFollowing> userFollowingList = new ArrayList<UserFollowing>();
        for (UserFollowingEntity entity : entityList) {
            userFollowingList.add(UserFollowing.FromEntity(entity));
        }
        return userFollowingList;
    }

    public static List<UserFollowingEntity> ToEntityList(Iterable<UserFollowing> userFollowings) {
        if (userFollowings == null) return null;
        List<UserFollowingEntity> entityList = new ArrayList<UserFollowingEntity>();
        for (UserFollowing userFollowing : userFollowings) {
            entityList.add(UserFollowing.ToEntity(userFollowing));
        }
        return entityList;
    }
}
