package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.api.dto.basic.Token;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class AccountLoginResult {

    @JsonProperty("permanent_token")
    private Token permanentToken;

    @JsonProperty("access_token")
    private Token accessToken;


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
