package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
public class UserPointTransactionObject {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("point")
    private Integer point;

    @JsonProperty("is_increase")
    private Boolean isIncrease;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonProperty("created_datetime")
    private DateTime createdDateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Boolean isIncrease() {
        return isIncrease;
    }

    public void setIsIncrease(Boolean isIncrease) {
        this.isIncrease = isIncrease;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

}
