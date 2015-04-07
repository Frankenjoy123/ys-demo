package com.yunsoo.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 存储用户收藏的产品
 * Created by Zhe on 2015/4/3.
 */
@Entity
@Table(name = "user_liked_product")
public class UserLikedProductEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "base_product_id")
    private int baseProductId;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_datetime", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "location")
    private String location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
