package com.yunsoo.api.auth.service;

import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.api.dto.OAuthAccount;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by yan on 12/12/2016.
 */
@Service
public class OAuthAccountService {
    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private AuthApiClient authApiClient;

    public OAuthAccount getOAuthAccountById(String accountId){
        if (StringUtils.isEmpty(accountId)) {
            return null;
        }
        try {
            return authApiClient.get("oauth/account/{id}", OAuthAccount.class, accountId);
        } catch (NotFoundException ex) {
            log.warn("oauth account not found by id: " + accountId);
            return null;
        }
    }
}
