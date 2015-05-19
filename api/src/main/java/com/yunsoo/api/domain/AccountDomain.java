package com.yunsoo.api.domain;

import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.RandomUtils;
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

    public void updatePassword(String accountId, String rawNewPassword) {
        String hashSalt = RandomUtils.generateString(8);
        String password = hashPassword(rawNewPassword, hashSalt);
        AccountObject accountObject = new AccountObject();
        accountObject.setPassword(password);
        accountObject.setHashSalt(hashSalt);
        dataAPIClient.patch("{id}", accountObject, accountId);
    }

    public boolean validPassword(String rawPassword, String hashSalt, String password) {
        return rawPassword != null && hashSalt != null && hashPassword(rawPassword, hashSalt).equals(password);
    }

    private String hashPassword(String rawPassword, String hashSalt) {
        return HashUtils.sha1HexString(rawPassword + hashSalt);
    }

}
