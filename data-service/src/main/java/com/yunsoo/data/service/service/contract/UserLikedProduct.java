package com.yunsoo.data.service.service.contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.data.service.entity.UserLikedProductEntity;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zhe on 2015/4/3.
 */
public class UserLikedProduct {

    private Long id;
    private String userId;
    private String baseProductId;
    private Boolean active;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    private String location;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime lastUpdatedDateTime;

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

    public String getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(String baseProductId) {
        this.baseProductId = baseProductId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public DateTime getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(DateTime lastUpdatedDateTime) {
        this.lastUpdatedDateTime = lastUpdatedDateTime;
    }

    public static UserLikedProductEntity ToEntity(UserLikedProduct userLikedProduct) {
        if (userLikedProduct == null) return null;

//        UserLikedProductEntity entity = new UserLikedProductEntity(userLikedProduct.getUserId(), userLikedProduct.getBaseProductId(),
//                userLikedProduct.getLocation(), userLikedProduct.getActive());
        UserLikedProductEntity entity = new UserLikedProductEntity();
        BeanUtils.copyProperties(userLikedProduct, entity, "createdDateTime", "id");
        if (userLikedProduct.getId() != null && userLikedProduct.getId() != 0) {
            entity.setId(userLikedProduct.getId());
        }
        if (userLikedProduct.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(userLikedProduct.getCreatedDateTime());
        } else {
            entity.setCreatedDateTime(DateTime.now());
        }

        return entity;
    }

    /**
     * @param entity
     * @return
     */
    public static UserLikedProduct FromEntity(UserLikedProductEntity entity) {
        if (entity == null) return null;

        UserLikedProduct userLikedProduct = new UserLikedProduct();
        BeanUtils.copyProperties(entity, userLikedProduct, "createdDateTime");
        if (entity.getCreatedDateTime() != null) {
            userLikedProduct.setCreatedDateTime(entity.getCreatedDateTime());
        }
        return userLikedProduct;
    }

    public static List<UserLikedProduct> FromEntityList(Iterable<UserLikedProductEntity> entityList) {
        if (entityList == null) return null;
        List<UserLikedProduct> userLikedProductList = new ArrayList<UserLikedProduct>();
        for (UserLikedProductEntity entity : entityList) {
            userLikedProductList.add(UserLikedProduct.FromEntity(entity));
        }
        return userLikedProductList;
    }

    public static List<UserLikedProductEntity> ToEntityList(Iterable<UserLikedProduct> userLikedProductList) {
        if (userLikedProductList == null) return null;
        List<UserLikedProductEntity> entityList = new ArrayList<UserLikedProductEntity>();
        for (UserLikedProduct userLikedProduct : userLikedProductList) {
            entityList.add(UserLikedProduct.ToEntity(userLikedProduct));
        }
        return entityList;
    }
}
