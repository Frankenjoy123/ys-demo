package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.UserLikedProductEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zhe on 2015/4/3.
 */
public class UserLikedProduct {

    private Long id;
    private long userId;
    private long baseProductId;
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

    public long getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(long baseProductId) {
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

        UserLikedProductEntity entity = new UserLikedProductEntity(userLikedProduct.getUserId(), userLikedProduct.getBaseProductId(),
                userLikedProduct.getLocation(), userLikedProduct.getActive());

//        BeanUtils.copyProperties(userLikedProduct, entity, new String[]{"createdDateTime", "id"});
//        if (userLikedProduct.getId() != null && userLikedProduct.getId() != 0) {
//            entity.setId(userLikedProduct.getId());
//        }
//        if (userLikedProduct.getCreatedDateTime() != null) {
//            entity.setCreatedDateTime(DateTime.parse(userLikedProduct.getCreatedDateTime()));
//        }else {
//            entity.setCreatedDateTime(DateTime.now());
//        }

        return entity;
    }

    /**
     * @param entity
     * @return
     */
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
