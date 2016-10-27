package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 10/27/2016.
 */
public class OAuthAccountLoginResponse {

    @JsonProperty("oauth_account_id")
    private String oauthAccountId;

    @JsonProperty("token")
    private String token;

    @JsonProperty("access_token")
    private Token accessToken;

    public String getOauthAccountId() {
        return oauthAccountId;
    }

    public void setOauthAccountId(String oauthAccountId) {
        this.oauthAccountId = oauthAccountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }
}
