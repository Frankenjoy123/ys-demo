package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2015/8/26
 * Descriptions:
 */
public class UserLoginResponse {

    @JsonProperty("token")
    private Token token;

    @JsonProperty("user_id")
    private String userId;


    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
