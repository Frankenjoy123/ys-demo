package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.security.authentication.TokenAuthenticationService;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.auth.service.LoginService;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.security.authentication.AuthAccount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created by:   Lijian
 * Created on:   2016-10-18
 * Descriptions:
 */
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private static int LOGIN_TOKEN_EXPIRES_SECONDS = 1440;

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @RequestMapping("/login")
    public void login() {

    }

    @RequestMapping("/bind")
    public void bind(@RequestParam(value = "login_token") String loginToken) {


    }

    @RequestMapping("/loginToken")
    public Token getLoginToken(@RequestParam(value = "account_id", required = false) String accountId,
                               @RequestParam(value = "source_type_code", required = false) String sourceTypeCode,
                               @RequestParam(value = "source", required = false) String source) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        accountId = AuthUtils.fixAccountId(accountId);

        log.info(String.format("login token creation request from account [id: %s] for account [id: %s]", currentAccountId, accountId));

        Account account;

        account = loginService.login(accountId);
        if (account == null) {
            throw new BadRequestException("account is not valid");
        }

        AuthUtils.checkPermission(account.getOrgId(), "logintoken", "create");
        AuthAccount authAccount = new AuthAccount();
        authAccount.setId(accountId);
        authAccount.setOrgId(account.getOrgId());
        if (sourceTypeCode != null && sourceTypeCode.equals("agency")) {
            authAccount.setDetails(new HashMap<>());
            authAccount.getDetails().put("source_type_code", sourceTypeCode);
            authAccount.getDetails().put("source", source);
        }
        return tokenAuthenticationService.generateLoginToken(authAccount, LOGIN_TOKEN_EXPIRES_SECONDS);
    }

    /**
     * @param oAuthAccountId
     * @param token          contains accountId,orgId,oauth_account_id
     * @return
     */
    @RequestMapping("accessToken")
    public Token getAccessToken(@RequestParam("oauth_account_id") String oAuthAccountId,
                                 @RequestParam("token") String token) {

        return null;
    }


}
