package com.yunsoo.api.rabbit.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.UserProductFollowingObject;
import org.joda.time.DateTime;

/**
 * Created by:   yan
 * Created on:   8/18/2015
 * Descriptions:
 */
public class UserProductFollowing {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("product_base_name")
    private String productName;

    @JsonProperty("product_base_desc")
    private String productBaseDesc;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBaseDesc() {
        return productBaseDesc;
    }

    public void setProductBaseDesc(String productBaseDesc) {
        this.productBaseDesc = productBaseDesc;
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
            this.userId = object.getUserId();
            this.productBaseId = object.getProductBaseId();
            this.createdDateTime = object.getCreatedDateTime();
        }
    }
}
