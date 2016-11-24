package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.third.dao.entity.ThirdWeChatAccessTokenEntity;

/**
 * Created by yan on 10/19/2016.
 */
public class WeChatAccessToken extends WeChatBaseType{

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("ticket")    //only available when get js api ticket;
    private String ticket;

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

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

    public WeChatAccessToken(){}


    public WeChatAccessToken(ThirdWeChatAccessTokenEntity token){
        this.setAccessToken(token.getAccessToken());
        this.setTicket(token.getJsapiTicket());
    }
}
