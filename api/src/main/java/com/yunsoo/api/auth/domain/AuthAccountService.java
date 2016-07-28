package com.yunsoo.api.auth.domain;

import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.api.auth.dto.AccountCreationRequest;
import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-07-27
 * Descriptions:
 */
@Service
public class AuthAccountService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private AuthApiClient authApiClient;


    public Account getById(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            return null;
        }
        try {
            return authApiClient.get("account/{id}", Account.class, accountId);
        } catch (NotFoundException ex) {
            log.warn("account not found by id: " + accountId);
            return null;
        }
    }

    public Account create(AccountCreationRequest request) {
        //todo

        return null;
    }


}
