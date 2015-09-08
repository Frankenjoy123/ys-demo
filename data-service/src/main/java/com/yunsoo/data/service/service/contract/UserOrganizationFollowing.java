package com.yunsoo.data.service.service.contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.data.service.entity.UserOrganizationFollowingEntity;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/4/15.
 */
public class UserOrganizationFollowing {

    private Long id;
    private String userId;
    private String orgId;
    private Boolean isFollowing;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime modifiedDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public static UserOrganizationFollowingEntity ToEntity(UserOrganizationFollowing userOrganizationFollowing) {
        if (userOrganizationFollowing == null) return null;

        UserOrganizationFollowingEntity entity = new UserOrganizationFollowingEntity();
        BeanUtils.copyProperties(userOrganizationFollowing, entity, "createdDateTime");
//        if (userOrganizationFollowing.getId() != null && userOrganizationFollowing.getId() != 0) {
//            entity.setId(userOrganizationFollowing.getId());
//        }
        if (userOrganizationFollowing.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(userOrganizationFollowing.getCreatedDateTime());
        }
        return entity;
    }

    public static UserOrganizationFollowing FromEntity(UserOrganizationFollowingEntity entity) {
        if (entity == null) return null;

        UserOrganizationFollowing userOrganizationFollowing = new UserOrganizationFollowing();
        BeanUtils.copyProperties(entity, userOrganizationFollowing, "createdDateTime");
        if (entity.getCreatedDateTime() != null) {
            userOrganizationFollowing.setCreatedDateTime(entity.getCreatedDateTime());
        }
        return userOrganizationFollowing;
    }

    public static List<UserOrganizationFollowing> FromEntityList(Iterable<UserOrganizationFollowingEntity> entityList) {
        if (entityList == null) return null;
        List<UserOrganizationFollowing> userOrganizationFollowingList = new ArrayList<>();
        for (UserOrganizationFollowingEntity entity : entityList) {
            userOrganizationFollowingList.add(UserOrganizationFollowing.FromEntity(entity));
        }
        return userOrganizationFollowingList;
    }

    public static List<UserOrganizationFollowingEntity> ToEntityList(Iterable<UserOrganizationFollowing> userFollowings) {
        if (userFollowings == null) return null;
        List<UserOrganizationFollowingEntity> entityList = new ArrayList<>();
        for (UserOrganizationFollowing userOrganizationFollowing : userFollowings) {
            entityList.add(UserOrganizationFollowing.ToEntity(userOrganizationFollowing));
        }
        return entityList;
    }
}
