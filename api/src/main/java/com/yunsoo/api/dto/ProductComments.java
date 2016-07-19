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

    @JsonProperty("ys_id")
    private String ysId;

    @JsonProperty("scan_record_id")
    private String scanRecordId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("address")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


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

    public String getYsId() {
        return ysId;
    }

    public void setYsId(String ysId) {
        this.ysId = ysId;
    }

    public String getScanRecordId() {
        return scanRecordId;
    }

    public void setScanRecordId(String scanRecordId) {
        this.scanRecordId = scanRecordId;
    }

    public ProductComments(ProductCommentsObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setProductBaseId(object.getProductBaseId());
            this.setComments(object.getComments());
            this.setScore(object.getScore());
            this.setUserId(object.getUserId());
            this.setYsId(object.getYsId());
            this.setScanRecordId(object.getScanRecordId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setAddress(object.getAddress());
        }
    }

    public ProductCommentsObject toProductCommentsObject() {
        ProductCommentsObject object = new ProductCommentsObject();
        object.setId(this.getId());
        object.setProductBaseId(this.getProductBaseId());
        object.setComments(this.getComments());
        object.setScore(this.getScore());
        object.setUserId(this.getUserId());
        object.setYsId(this.getYsId());
        object.setScanRecordId(this.getScanRecordId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setAddress(this.getAddress());
        return object;
    }
}
