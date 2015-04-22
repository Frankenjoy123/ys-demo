package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * 存储用户收藏的产品
 * Created by Zhe on 2015/4/3.
 */
@Entity
@Table(name = "user_liked_product")
public class UserLikedProductEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "base_product_id")
    private String baseProductId;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_datetime", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "location")
    private String location;

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

    public UserLikedProductEntity() {
    }

    public UserLikedProductEntity(String userId, String baseProductId, String location, Boolean active) {
        this.userId = userId;
        this.baseProductId = baseProductId;
        this.location = location;
        this.active = active;
        this.createdDateTime = DateTime.now();
    }
}
