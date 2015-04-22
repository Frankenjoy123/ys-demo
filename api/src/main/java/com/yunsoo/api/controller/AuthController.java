package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountTokenDomain;
import com.yunsoo.api.dto.AccountLoginRequest;
import com.yunsoo.api.dto.AccountLoginResult;
import com.yunsoo.api.dto.basic.Account;
import com.yunsoo.api.dto.basic.Token;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AccountTokenObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private AccountTokenDomain accountTokenDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AccountLoginResult login(
            @RequestHeader("X-YS-AppId") String appId,
            @RequestHeader(value = "X-YS-DeviceId", required = false) String deviceId,
            @RequestBody AccountLoginRequest account) {
        if (account.getAccountId() == null && (account.getOrgId() == null || account.getIdentifier() == null)) {
            throw new BadRequestException("Account is not valid");
        }
        AccountObject accountObject = account.getAccountId() != null
                ? accountDomain.getById(account.getAccountId())
                : accountDomain.getByOrgIdAndIdentifier(account.getOrgId(), account.getIdentifier());
        if (accountObject == null) {
            throw new UnauthorizedException("Account is not valid");
        }
        String rawPassword = account.getPassword();
        String password = accountObject.getPassword();
        String hashSalt = accountObject.getHashSalt();

        if (!HashUtils.sha1(rawPassword + hashSalt).equals(password)) {
            throw new UnauthorizedException("Account is not valid");
        }

        //login successfully

        AccountTokenObject accountTokenObject = accountTokenDomain.createPermanentToken(accountObject.getId(), appId, deviceId);

        AccountLoginResult result = new AccountLoginResult();
        result.setPermanentToken(new Token(accountTokenObject.getPermanentToken(), accountTokenObject.getPermanentTokenExpiresDateTime()));
        result.setAccessToken(new Token("0723f205f20a8ed6e06c9c51aad640807ec0cde691e459951ca4f7eb804ae6b2", DateTime.now().plusMinutes(10)));
        return result;
    }

    @RequestMapping(value = "/accessToken/refresh", method = RequestMethod.GET)
    public Token refreshToken(@RequestParam("permanent_token") String permanentToken) {
        AccountTokenObject accountTokenObject = accountTokenDomain.getByPermanentToken(permanentToken);
        if (accountTokenObject == null || accountTokenObject.getPermanentTokenExpiresDateTime().isBeforeNow()) {
            throw new UnauthorizedException("permanent_token invalid");
        }

        //todo: generate access token
        String accountId = accountTokenObject.getAccountId();
        DateTime now = DateTime.now();
        DateTime expires = now.plusMinutes(10);
        String accessToken = "0723f205f20a8ed6e06c9c51aad640807ec0cde691e459951ca4f7eb804ae6b2";

        return new Token(accessToken, expires);
    }

    public Account getCurrentAccount() {
        //        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof AccountAuthentication) {
//            return ((AccountAuthentication) authentication).getDetails();
//        }
        return null;
    }
}
