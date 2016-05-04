package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.ProductCommentsObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2015/8/21
 * Descriptions:
 */
public class ProductComments {

    @JsonProperty("id")
    private String id;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("score")
    private Integer score;

    @JsonProperty("user_id")
    private String userId;


    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ProductComments(ProductCommentsObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setProductBaseId(object.getProductBaseId());
            this.setComments(object.getComments());
            this.setScore(object.getScore());
            this.setUserId(object.getUserId());
            this.setCreatedDateTime(object.getCreatedDateTime());
        }
    }

    public ProductCommentsObject toProductCommentsObject() {
        ProductCommentsObject object = new ProductCommentsObject();
        object.setId(this.getId());
        object.setProductBaseId(this.getProductBaseId());
        object.setComments(this.getComments());
        object.setScore(this.getScore());
        object.setUserId(this.getUserId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        return object;
    }
}
