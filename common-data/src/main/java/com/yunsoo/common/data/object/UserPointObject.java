package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
public class UserPointObject {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("point")
    private Integer point;

    @JsonProperty("last_sign_in_datetime")
    private DateTime lastSignInDateTime;

    @JsonProperty("continuous_sign_in_days")
    private Integer continuousSignInDays;


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

    public DateTime getLastSignInDateTime() {
        return lastSignInDateTime;
    }

    public void setLastSignInDateTime(DateTime lastSignInDateTime) {
        this.lastSignInDateTime = lastSignInDateTime;
    }

    public Integer getContinuousSignInDays() {
        return continuousSignInDays;
    }

    public void setContinuousSignInDays(Integer continuousSignInDays) {
        this.continuousSignInDays = continuousSignInDays;
    }
}