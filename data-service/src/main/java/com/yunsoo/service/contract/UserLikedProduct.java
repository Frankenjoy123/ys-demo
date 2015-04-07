package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.UserModel;
import com.yunsoo.entity.UserLikedProductEntity;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * Created by Zhe on 2015/4/3.
 */
public class UserLikedProduct {

    private Long id;
    private long userId;
    private int baseProductId;
    private Boolean active;
    private String createdDateTime;
    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(int baseProductId) {
        this.baseProductId = baseProductId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static UserLikedProductEntity ToEntity(UserLikedProduct userLikedProduct) {
        if (userLikedProduct == null) return null;

        UserLikedProductEntity entity = new UserLikedProductEntity();
        BeanUtils.copyProperties(userLikedProduct, entity, new String[]{"createdDateTime", "id"});
        if (userLikedProduct.getId() != null && userLikedProduct.getId() != 0) {
            entity.setId(userLikedProduct.getId());
        }
        if (userLikedProduct.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(DateTime.parse(userLikedProduct.getCreatedDateTime()));
        }

        return entity;
    }

    public static UserLikedProduct FromEntity(UserLikedProductEntity entity) {
        if (entity == null) return null;

        UserLikedProduct userLikedProduct = new UserLikedProduct();
        BeanUtils.copyProperties(entity, userLikedProduct, new String[]{"createdDateTime"});
        if (entity.getCreatedDateTime() != null) {
            userLikedProduct.setCreatedDateTime(entity.getCreatedDateTime().toString());
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
