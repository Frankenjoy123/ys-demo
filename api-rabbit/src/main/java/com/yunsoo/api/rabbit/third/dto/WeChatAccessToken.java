package com.yunsoo.api.rabbit.third.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Admin on 6/28/2016.
 */
public class WeChatAccessToken implements Serializable {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("jsapi_ticket")
    private String jsapiTicket;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("expired_datetime")
    private DateTime expiredDatetime;

    @JsonProperty("app_id")
    private String appId;

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

    public DateTime getExpiredDatetime() {
        return expiredDatetime;
    }

    public void setExpiredDatetime(DateTime expiredDatetime) {
        this.expiredDatetime = expiredDatetime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
