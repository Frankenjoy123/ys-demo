package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.security.authentication.TokenAuthenticationService;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.auth.service.LoginService;
import com.yunsoo.common.web.exception.BadRequestException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@RestController
@RequestMapping("/logintoken")
public class LoginTokenController {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * generate loginToken for the given account, must check the permission with current login account
     *
     * @param accountId String
     * @param expiresIn Integer, seconds
     * @return loginToken Token
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Token getLoginToken(@RequestParam(value = "account_id", required = false) String accountId,
                               @RequestParam(value = "expires_in", required = false) Integer expiresIn) {

        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        accountId = AuthUtils.fixAccountId(accountId);

        log.info(String.format("login token creation request from account [id: %s] for account [id: %s]", currentAccountId, accountId));

        Account account;

        account = loginService.login(accountId);
        if (account == null) {
            throw new BadRequestException("account is not valid");
        }

        AuthUtils.checkPermission(account.getOrgId(), "logintoken", "create");

        return tokenAuthenticationService.generateLoginToken(accountId, expiresIn);
    }

}
