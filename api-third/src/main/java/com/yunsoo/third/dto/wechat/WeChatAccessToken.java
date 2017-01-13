package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import com.yunsoo.third.dao.entity.ThirdWeChatAccessTokenEntity;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by yan on 10/19/2016.
 */
public class WeChatAccessToken implements Serializable {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("jsapi_ticket")
    private String ticket;

    @JsonProperty("expired_datetime")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime expiredDatetime;

    @JsonProperty("app_id")
    private String appId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
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

    public WeChatAccessToken(){}


    public WeChatAccessToken(ThirdWeChatAccessTokenEntity token){
        this.setAccessToken(token.getAccessToken());
        this.setTicket(token.getJsapiTicket());
        this.setExpiredDatetime(token.getExpiredDatetime());
        this.setAppId(token.getAppId());
    }
}
