package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 12/7/2016.
 */
public class WeChatToken extends WeChatBaseType {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
