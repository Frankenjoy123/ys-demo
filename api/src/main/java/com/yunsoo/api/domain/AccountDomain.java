package com.yunsoo.api.domain;

import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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


    public AccountObject getById(String accountId) {
        try {
            return dataAPIClient.get("account/{id}", AccountObject.class, accountId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public AccountObject getByOrgIdAndIdentifier(String orgId, String identifier) {
        AccountObject[] accountObjects = dataAPIClient.get("account?org_id={0}&identifier={1}", AccountObject[].class, orgId, identifier);
        if (accountObjects == null || accountObjects.length <= 0) {
            return null;
        } else if (accountObjects.length > 1) {
            throw new InternalServerErrorException("duplicated account found by [orgId: " + orgId + ", identifier: " + identifier + "]");
        }
        return accountObjects[0];
    }

    public List<AccountObject> getByOrgId(String orgId) {
        AccountObject[] accountObjects = dataAPIClient.get("account?org_id={0}", AccountObject[].class, orgId);
        return Arrays.asList(accountObjects);
    }


    public Boolean validateToken(String token, String orgId) {
        if (token == null || token.isEmpty()) return false;
        if (orgId == null || orgId.isEmpty()) return false;
        return tokenAuthenticationService.checkOrgResource(token, orgId, true);
    }
}
