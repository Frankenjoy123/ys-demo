package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
public class UserActivityObject implements Serializable {

    @JsonProperty("user_id")
    private String userId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("last_sign_in_datetime")
    private DateTime lastSignInDateTime;

    @JsonProperty("last_sign_in_continuous_days")
    private Integer lastSignInContinuousDays;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DateTime getLastSignInDateTime() {
        return lastSignInDateTime;
    }

    public void setLastSignInDateTime(DateTime lastSignInDateTime) {
        this.lastSignInDateTime = lastSignInDateTime;
    }

    public Integer getLastSignInContinuousDays() {
        return lastSignInContinuousDays;
    }

    public void setLastSignInContinuousDays(Integer lastSignInContinuousDays) {
        this.lastSignInContinuousDays = lastSignInContinuousDays;
    }
}