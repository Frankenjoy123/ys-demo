package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountTokenDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.AccountLoginRequest;
import com.yunsoo.api.dto.AccountLoginResponse;
import com.yunsoo.api.dto.basic.Token;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AccountTokenObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.UnauthorizedException;
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
    private OrganizationDomain organizationDomain;

    @Autowired
    private AccountTokenDomain accountTokenDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AccountLoginResponse login(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestBody AccountLoginRequest account) {
        if (account.getAccountId() == null && (account.getOrganization() == null || account.getIdentifier() == null)) {
            throw new BadRequestException("Account is not valid");
        }

        AccountObject accountObject;
        if (account.getAccountId() != null) {
            accountObject = accountDomain.getById(account.getAccountId().trim());
        } else {
            String org = account.getOrganization().trim();
            OrganizationObject orgObject = null;
            if (org.length() == 19) {
                orgObject = organizationDomain.getOrganizationById(org);
            }
            if (orgObject == null) {
                orgObject = organizationDomain.getOrganizationByName(org);
            }
            if (orgObject == null) {
                throw new UnauthorizedException("Account is not valid");
            }
            accountObject = accountDomain.getByOrgIdAndIdentifier(orgObject.getId(), account.getIdentifier().trim());
        }

        if (accountObject == null) {
            throw new UnauthorizedException("Account is not valid");
        }
        String accountId = accountObject.getId();
        String orgId = accountObject.getOrgId();
        String identifier = accountObject.getIdentifier();
        String rawPassword = account.getPassword();
        String password = accountObject.getPassword();
        String hashSalt = accountObject.getHashSalt();

        if (!accountDomain.validPassword(rawPassword, hashSalt, password)) {
            throw new UnauthorizedException("Account is not valid");
        }
        //login successfully
        LOGGER.info("Account login with password [id: {}, orgId: {}, identifier: {}]", accountId, orgId, identifier);

        AccountTokenObject accountTokenObject = accountTokenDomain.create(accountId, appId, deviceId);

        AccountLoginResponse result = new AccountLoginResponse();
        result.setPermanentToken(new Token(accountTokenObject.getPermanentToken(), accountTokenObject.getPermanentTokenExpiresDateTime()));
        result.setAccessToken(tokenAuthenticationService.generateAccessToken(accountId, orgId));
        return result;
    }

    @RequestMapping(value = "/accessToken/refresh", method = RequestMethod.GET)
    public Token refreshToken(@RequestParam("permanent_token") String permanentToken) {
        AccountTokenObject accountTokenObject = accountTokenDomain.getByPermanentToken(permanentToken);
        if (accountTokenObject == null || accountTokenObject.getPermanentTokenExpiresDateTime().isBeforeNow()) {
            throw new UnauthorizedException("permanent_token invalid");
        }
        String accountId = accountTokenObject.getAccountId();
        AccountObject accountObject = accountDomain.getById(accountId);
        String orgId = accountObject.getOrgId();

        Token accessToken = tokenAuthenticationService.generateAccessToken(accountId, orgId);

        LOGGER.info("AccessToken refreshed for Account [id: {}]", accountTokenObject.getAccountId());

        return accessToken;
    }


}
