package com.yunsoo.api.auth.service;

import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-07-28
 * Descriptions:
 */
@Service
public class AuthTokenService {

    @Autowired
    private AuthApiClient authApiClient;

    public Account parseAccessToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            return authApiClient.post("accessToken", token, Account.class);
        } catch (UnauthorizedException e) {
            return null;
        }
    }
}
