package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.UserProductFollowingObject;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

/**
 * Created by:   yan
 * Created on:   8/18/2015
 * Descriptions:
 */
public class UserProductFollowing {

    @JsonProperty("user_id")
    private String userId;

    @NotEmpty(message = "product_base_id must not be null or empty")
    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("product_base_name")
    private String productBaseName;

    @JsonProperty("product_base_desc")
    private String productBaseDesc;

    @JsonProperty("product_base_image_url")
    private String productBaseImageUrl;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("comments_count")
    private Long commentsCount;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;


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

    public String getProductBaseName() {
        return productBaseName;
    }

    public void setProductBaseName(String productBaseName) {
        this.productBaseName = productBaseName;
    }

    public String getProductBaseDesc() {
        return productBaseDesc;
    }

    public void setProductBaseDesc(String productBaseDesc) {
        this.productBaseDesc = productBaseDesc;
    }

    public String getProductBaseImageUrl() {
        return productBaseImageUrl;
    }

    public void setProductBaseImageUrl(String productBaseImageUrl) {
        this.productBaseImageUrl = productBaseImageUrl;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public UserProductFollowing() {
    }

    public UserProductFollowing(UserProductFollowingObject object) {
        if (object != null) {
            this.setUserId(object.getUserId());
            this.setProductBaseId(object.getProductBaseId());
            this.setCreatedDateTime(object.getCreatedDateTime());
        }
    }
}
