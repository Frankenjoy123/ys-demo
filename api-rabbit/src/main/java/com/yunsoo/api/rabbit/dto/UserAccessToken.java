package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.UserAccessTokenObject;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by Admin on 6/29/2016.
 */
public class UserAccessToken {

    @JsonProperty("id")
    private String id;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("jsapi_ticket")
    private String jsapiTicket;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("expired_datetime")
    private DateTime expiredDatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getExpiredDatetime() {
        return expiredDatetime;
    }

    public void setExpiredDatetime(DateTime expiredDatetime) {
        this.expiredDatetime = expiredDatetime;
    }

    public UserAccessToken() {
    }

    public UserAccessToken(UserAccessTokenObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setJsapiTicket(object.getJsapiTicket());
            this.setOrgId(object.getOrgId());
            this.setAccessToken(object.getAccessToken());
            this.setExpiredDatetime(object.getExpiredDatetime());
        }
    }

    public UserAccessTokenObject toUserAccessTokenObject() {
        UserAccessTokenObject object = new UserAccessTokenObject();
        object.setId(this.getId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setJsapiTicket(this.getJsapiTicket());
        object.setOrgId(this.getOrgId());
        object.setAccessToken(this.getAccessToken());
        object.setExpiredDatetime(this.getExpiredDatetime());

        return object;
    }
}
