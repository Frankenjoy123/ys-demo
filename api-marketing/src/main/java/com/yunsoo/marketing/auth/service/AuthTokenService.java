package com.yunsoo.marketing.auth.service;

import com.yunsoo.common.web.exception.UnauthorizedException;
import com.yunsoo.marketing.auth.dto.Account;
import com.yunsoo.marketing.client.AuthApiClient;
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
