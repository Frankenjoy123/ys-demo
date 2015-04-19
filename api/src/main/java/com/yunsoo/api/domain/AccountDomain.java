package com.yunsoo.api.domain;

import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Zhe on 2015/4/19.
 */
@Component
public class AccountDomain {

    @Autowired
    private RestClient dataAPIClient;
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDomain.class);

    public Boolean validateToken(String token, String orgId) {
        if (token == null || token.isEmpty()) return false;
        if (orgId == null || orgId.isEmpty()) return false;
        return tokenAuthenticationService.checkOrgResource(token, orgId, true);
    }
}
