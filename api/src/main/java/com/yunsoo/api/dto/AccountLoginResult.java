package com.yunsoo.api.dto;

import com.yunsoo.api.dto.basic.Token;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class AccountLoginResult {

    private Token permanentToken;

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
