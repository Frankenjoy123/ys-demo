package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.UserConfigObject;
import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/9/16
 * Descriptions:
 */
public class UserConfig {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("auto_following")
    private boolean autoFollowing;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAutoFollowing() {
        return autoFollowing;
    }

    public void setAutoFollowing(boolean autoFollowing) {
        this.autoFollowing = autoFollowing;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }


    public UserConfig() {
    }

    public UserConfig(UserConfigObject object) {
        if (object != null) {
            this.userId = object.getUserId();
            this.autoFollowing = object.isAutoFollowing();
            this.modifiedDateTime = object.getModifiedDateTime();
        }
    }
}
