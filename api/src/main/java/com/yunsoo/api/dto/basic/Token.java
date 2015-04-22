package com.yunsoo.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/22
 * Descriptions:
 */
public class Token {

    @JsonProperty("token")
    String token;

    /**
     * seconds
     */
    @JsonProperty("expires_in")
    Long expiresIn;


    public Token() {
    }

    public Token(String token, Long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public Token(String token, DateTime expiresOn) {
        this.token = token;
        if (expiresOn != null) {
            this.expiresIn = (expiresOn.getMillis() / DateTime.now().getMillis()) / 1000;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
