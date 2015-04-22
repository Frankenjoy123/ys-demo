package com.yunsoo.api.domain;

import com.yunsoo.api.dto.basic.Account;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/19
 * Descriptions:
 */
@Component
public class AccountDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDomain.class);


    public AccountObject getById(String accountId) {
        return dataAPIClient.get("account/{id}", AccountObject.class, accountId);
    }

    public AccountObject getByOrgIdAndIdentifier(String orgId, String identifier) {
        return dataAPIClient.get("account?orgId={0}&identifier={1}", AccountObject.class, orgId, identifier);
    }

    public Boolean validateToken(String token, String orgId) {
        if (token == null || token.isEmpty()) return false;
        if (orgId == null || orgId.isEmpty()) return false;
        return tokenAuthenticationService.checkOrgResource(token, orgId, true);
    }
}
