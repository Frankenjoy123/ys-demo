package com.yunsoo.auth.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class AccountLoginResponse {

    @JsonProperty("permanent_token")
    private Token permanentToken;

    @JsonProperty("access_token")
    private Token accessToken;

    public AccountLoginResponse() {
    }

    public AccountLoginResponse(Token permanentToken, Token accessToken) {
        this.permanentToken = permanentToken;
        this.accessToken = accessToken;
    }

    public Token getPermanentToken() {
        return permanentToken;
    }

    public void setPermanentToken(Token permanentToken) {
        this.permanentToken = permanentToken;
    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }
}
