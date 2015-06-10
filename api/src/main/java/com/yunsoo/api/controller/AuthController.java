package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountTokenDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.dto.AccountLoginRequest;
import com.yunsoo.api.dto.AccountLoginResponse;
import com.yunsoo.api.dto.Token;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AccountTokenObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/10
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
    private PermissionDomain permissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    /**
     * Login with password
     *
     * @param appId    String
     * @param deviceId String
     * @param account  AccountLoginRequest
     * @return AccountLoginResponse include permanentToken and accessToken
     */
    @RequestMapping(value = "login/password", method = RequestMethod.POST)
    public AccountLoginResponse login(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestBody @Valid AccountLoginRequest account) {

        if (account.getAccountId() == null && (account.getOrganization() == null || account.getIdentifier() == null)) {
            throw new UnauthorizedException("Account is not valid");
        }

        AccountObject accountObject;
        if (account.getAccountId() != null) {
            accountObject = accountDomain.getById(account.getAccountId().trim());
        } else {
            String org = account.getOrganization().trim();
            OrganizationObject orgObject = null;
            if (org.length() == 19) {
                //org is orgId
                orgObject = organizationDomain.getOrganizationById(org);
            }
            if (orgObject == null) {
                //org is orgName
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

        AccountTokenObject accountTokenObject = accountTokenDomain.create(accountId, appId, deviceId);

        //login successfully
        LOGGER.info("Account login with password [id: {}, orgId: {}, identifier: {}]", accountId, orgId, identifier);

        AccountLoginResponse result = new AccountLoginResponse();
        result.setPermanentToken(new Token(accountTokenObject.getPermanentToken(), accountTokenObject.getPermanentTokenExpiresDateTime()));
        result.setAccessToken(tokenAuthenticationService.generateAccessToken(accountId, orgId));
        return result;
    }

    /**
     * Login with loginToken
     *
     * @param loginToken Token
     * @return AccountLoginResponse include permanentToken and accessToken
     */
    @RequestMapping(value = "login/token", method = RequestMethod.POST)
    public AccountLoginResponse login(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestBody Token loginToken) {
        String token = loginToken.getToken();
        if (StringUtils.isEmpty(token)) {
            throw new UnauthorizedException("Token is not valid");
        }
        TAccount tAccount = tokenAuthenticationService.parseLoginToken(token);
        if (tAccount == null) {
            throw new UnauthorizedException("Token is not valid");
        }

        String accountId = tAccount.getId();
        AccountObject accountObject = accountDomain.getById(accountId);
        if (accountObject == null) {
            throw new UnauthorizedException("Token is not valid");
        }
        String orgId = accountObject.getOrgId();
        String identifier = accountObject.getIdentifier();
        AccountTokenObject accountTokenObject = accountTokenDomain.create(accountId, appId, deviceId);

        //login successfully
        LOGGER.info("Account login with token [id: {}, orgId: {}, identifier: {}]", accountId, orgId, identifier);

        AccountLoginResponse result = new AccountLoginResponse();
        result.setPermanentToken(new Token(accountTokenObject.getPermanentToken(), accountTokenObject.getPermanentTokenExpiresDateTime()));
        result.setAccessToken(tokenAuthenticationService.generateAccessToken(accountId, orgId));
        return result;
    }

    /**
     * create new accessToken with valid permanentToken
     *
     * @param permanentToken Token
     * @return accessToken Token
     */
    @RequestMapping(value = "accesstoken", method = RequestMethod.POST)
    public Token getAccessToken(@RequestBody Token permanentToken) {
        if (permanentToken == null || StringUtils.isEmpty(permanentToken.getToken())) {
            throw new UnauthorizedException("permanent_token invalid");
        }
        AccountTokenObject accountTokenObject = accountTokenDomain.getByPermanentToken(permanentToken.getToken());
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

    @RequestMapping(value = "logintoken")
    public Token getLoginToken(@RequestParam("account_id") String accountId) {
        AccountObject accountObject = accountDomain.getById(accountId);
        if (accountObject == null) {
            throw new NotFoundException("Account not found by [id: " + accountId + "]");
        }
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        if (!permissionDomain.hasPermission(currentAccountId, new TPermission(accountObject.getOrgId(), "logintoken", "read"))) {
            throw new ForbiddenException();
        }

        return tokenAuthenticationService.generateLoginToken(accountId);
    }


}
