package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 10/27/2016.
 */
public class OAuthAccountLoginRequest {

    @JsonProperty("oauth_openid")
    private String oauthOpenid;

    @JsonProperty("oauth_open_type")
    private String oauthOpenType;

    @JsonProperty("login_token")
    private String loginToken;


    @JsonProperty("oauth_name")
    private String oauthName;

    @JsonProperty("oauth_gravatar_url")
    private String oauthGravatarUrl;

    public String getOauthName() {
        return oauthName;
    }

    public void setOauthName(String oauthName) {
        this.oauthName = oauthName;
    }

    public String getOauthGravatarUrl() {
        return oauthGravatarUrl;
    }

    public void setOauthGravatarUrl(String oauthGravatarUrl) {
        this.oauthGravatarUrl = oauthGravatarUrl;
    }

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

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

}
