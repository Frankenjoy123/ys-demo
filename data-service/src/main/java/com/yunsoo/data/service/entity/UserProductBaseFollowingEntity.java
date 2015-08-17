package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by yan on 8/7/2015.
 */
@Entity
@Table(name = "user_product_following")
public class UserProductBaseFollowingEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "product_base_id")
    private String productBaseId;

    @Column(name = "is_following")
    private Boolean isFollowing;

    @Column(name = "created_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "modified_time", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
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

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProductBaseFollowingEntity)) return false;

        UserProductBaseFollowingEntity entity = (UserProductBaseFollowingEntity) o;

        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (!userId.equals(entity.userId)) return false;
        return productBaseId.equals(entity.productBaseId);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + userId.hashCode();
        result = 31 * result + productBaseId.hashCode();
        return result;
    }
}
