package com.yunsoo.api.rabbit.dto;

/**
 * Created by Zhe on 2015/4/21.
 */
public class UserResult {
    private String token;
    private String userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserResult() {
    }

    public UserResult(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }
}
