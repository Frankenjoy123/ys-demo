package com.yunsoo.marketing.security.authentication;

import com.yunsoo.marketing.auth.dto.Account;
import com.yunsoo.marketing.auth.service.AuthTokenService;
import com.yunsoo.marketing.security.AuthAccount;
import com.yunsoo.marketing.security.authorization.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2016-07-28
 * Descriptions:
 */
@Service
public class TokenAuthenticationService {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private AuthTokenService authTokenService;

    public AccountAuthentication getAuthentication(String token) {
        if (token == null) {
            return null;
        }
        Account account = authTokenService.parseAccessToken(token);
        if (account == null) {
            return null;
        }
        AuthAccount authAccount = new AuthAccount();
        authAccount.setId(account.getId());
        authAccount.setOrgId(account.getOrgId());
        return new AccountAuthentication(authAccount, authorizationService).fillCredentials(token);
    }
}