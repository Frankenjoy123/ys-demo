package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 10/27/2016.
 */
public class OAuthAccountLoginRequest {

    @JsonProperty("oauth_open_id")
    private String oauthOpenid;

    @JsonProperty("oauth_open_type")
    private String oauthOpenType;

    @JsonProperty("oauth_token")
    private String oauthToken;

    @JsonProperty("login_token")
    private String loginToken;

    public String getOauthOpenid() {
        return oauthOpenid;
    }

    public void setOauthOpenid(String oauthOpenid) {
        this.oauthOpenid = oauthOpenid;
    }

    public String getOauthOpenType() {
        return oauthOpenType;
    }

    public void setOauthOpenType(String oauthOpenType) {
        this.oauthOpenType = oauthOpenType;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}
