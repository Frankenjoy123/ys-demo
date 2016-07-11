package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.security.authentication.TokenAuthenticationService;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.AccountToken;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.auth.service.AccountTokenService;
import com.yunsoo.auth.service.LoginService;
import com.yunsoo.common.web.exception.UnauthorizedException;
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
@RequestMapping("/accesstoken")
public class AccessTokenController {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private AccountTokenService accountTokenService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * generate accessToken with valid permanentToken
     *
     * @param permanentToken Token
     * @return accessToken Token
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Token getAccessToken(@RequestParam(value = "permanent_token") String permanentToken) {

        AccountToken accountToken = accountTokenService.getNonExpiredByPermanentToken(permanentToken);
        if (accountToken == null) {
            throw new UnauthorizedException("permanent_token invalid");
        }

        Account account = loginService.login(accountToken.getAccountId());
        if (account == null) {
            throw new UnauthorizedException("account is not valid");
        }

        Token accessToken = tokenAuthenticationService.generateAccessToken(account.getId(), account.getOrgId());

        log.info(String.format("access token generated for account [id: %s]", account.getId()));
        return accessToken;
    }
}
