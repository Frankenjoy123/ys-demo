package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.AccountTokenDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.AccountLoginRequest;
import com.yunsoo.api.dto.AccountLoginResponse;
import com.yunsoo.api.dto.Token;
import com.yunsoo.api.security.AuthAccount;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AccountTokenObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private Log log = LogFactory.getLog(this.getClass());

    /**
     * login with password
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

        log.info(String.format("password login request from [appId: %s, deviceId: %s]", appId, deviceId));

        //validate parameters
        if (account.getAccountId() == null && (account.getOrganization() == null || account.getIdentifier() == null)) {
            log.warn(String.format("parameters are invalid [accountId: %s, organization: %s, identifier: %s]",
                    account.getAccountId(), account.getOrganization(), account.getIdentifier()));
            throw new UnauthorizedException("account is not valid");
        }

        //find account
        AccountObject accountObject;
        if (account.getAccountId() != null && StringUtils.hasText(account.getAccountId())) {
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
                log.warn(String.format("organization not found, [org: %s]", org));
                throw new UnauthorizedException("account is not valid");
            }
            accountObject = accountDomain.getByOrgIdAndIdentifier(orgObject.getId(), account.getIdentifier().trim());
        }
        if (accountObject == null) {
            log.warn("account not found");
            throw new UnauthorizedException("account is not valid");
        }

        //prepare properties
        String accountId = accountObject.getId();
        String orgId = accountObject.getOrgId();
        String identifier = accountObject.getIdentifier();
        String rawPassword = account.getPassword();
        String password = accountObject.getPassword();
        String hashSalt = accountObject.getHashSalt();

        //validate password
        if (!accountDomain.validatePassword(rawPassword, hashSalt, password)) {
            log.warn(String.format("password not valid, [id: %s, orgId: %s, identifier: %s]", accountId, orgId, identifier));
            throw new UnauthorizedException("account is not valid");
        }

        //create account token
        //permanent token
        AccountTokenObject accountToken = accountTokenDomain.create(accountId, appId, deviceId);
        Token permanentToken = new Token(accountToken.getPermanentToken(), accountToken.getPermanentTokenExpiresDateTime());
        //access token
        Token accessToken = tokenAuthenticationService.generateAccessToken(accountId, orgId);

        //login successfully
        log.info(String.format("password login successfully, [id: %s, orgId: %s, identifier: %s, appId: %s, deviceId: %s]",
                accountId, orgId, identifier, appId, deviceId));
        return new AccountLoginResponse(permanentToken, accessToken);
    }

    /**
     * login with loginToken
     *
     * @param loginToken Token
     * @return AccountLoginResponse include permanentToken and accessToken
     */
    @RequestMapping(value = "login/token", method = RequestMethod.POST)
    public AccountLoginResponse login(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestBody Token loginToken) {

        log.info(String.format("token login request from [appId: %s, deviceId: %s]", appId, deviceId));

        String token = loginToken.getToken();
        if (StringUtils.isEmpty(token)) {
            log.warn(String.format("token is empty [token: %s]", token));
            throw new UnauthorizedException("token is not valid");
        }

        AuthAccount authAccount = tokenAuthenticationService.parseLoginToken(token);
        if (authAccount == null || StringUtils.isEmpty(authAccount.getId())) {
            log.warn(String.format("token is not valid [token: %s]", token));
            throw new UnauthorizedException("token is not valid");
        }

        String accountId = authAccount.getId();
        AccountObject accountObject = accountDomain.getById(accountId);
        if (accountObject == null) {
            log.warn("account not found");
            throw new UnauthorizedException("token is not valid");
        }

        //prepare properties
        String orgId = accountObject.getOrgId();
        String identifier = accountObject.getIdentifier();

        //create account token
        //permanent token
        AccountTokenObject accountToken = accountTokenDomain.create(accountId, appId, deviceId, null);
        Token permanentToken = new Token(accountToken.getPermanentToken(), accountToken.getPermanentTokenExpiresDateTime());
        //access token
        Token accessToken = tokenAuthenticationService.generateAccessToken(accountId, orgId);

        //login successfully
        log.info(String.format("token login successfully, [id: %s, orgId: %s, identifier: %s, appId: %s, deviceId: %s]",
                accountId, orgId, identifier, appId, deviceId));
        return new AccountLoginResponse(permanentToken, accessToken);
    }

    /**
     * generate accessToken with valid permanentToken
     *
     * @param permanentToken Token
     * @return accessToken Token
     */
    @RequestMapping(value = "accesstoken", method = RequestMethod.GET)
    public Token getAccessToken(@RequestParam(value = "permanent_token") String permanentToken) {

        if (StringUtils.isEmpty(permanentToken)) {
            throw new UnauthorizedException("permanent_token invalid");
        }

        AccountTokenObject accountToken = accountTokenDomain.getByPermanentToken(permanentToken);
        if (accountToken == null
                || (accountToken.getPermanentTokenExpiresDateTime() != null && accountToken.getPermanentTokenExpiresDateTime().isBeforeNow())) {
            throw new UnauthorizedException("permanent_token invalid");
        }

        String accountId = accountToken.getAccountId();
        AccountObject accountObject = accountDomain.getById(accountId);
        String orgId = accountObject.getOrgId();

        Token accessToken = tokenAuthenticationService.generateAccessToken(accountId, orgId);

        log.info(String.format("access token generated for account [id: %s]", accountToken.getAccountId()));
        return accessToken;
    }

    /**
     * generate loginToken for the given account, must check the permission with current login account
     *
     * @param accountId String
     * @param expiresIn Integer, seconds
     * @return loginToken Token
     */
    @RequestMapping(value = "logintoken", method = RequestMethod.GET)
    public Token getLoginToken(@RequestParam(value = "account_id", required = false) String accountId,
                               @RequestParam(value = "expires_in", required = false) Integer expiresIn) {

        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();

        log.info(String.format("login token creation request from account [id: %s] for account [id: %s]", currentAccountId, accountId));

        AccountObject accountObject = null;
        if (StringUtils.isEmpty(accountId) || "current".equals(accountId)) {
            accountId = currentAccountId;
        }
        accountObject = accountDomain.getById(accountId);
        if (accountObject == null) {
            throw new NotFoundException("account not found by [id: " + accountId + "]");
        }

        if (!accountPermissionDomain.hasPermission(currentAccountId, new TPermission(accountObject.getOrgId(), "logintoken", "create"))) {
            throw new ForbiddenException();
        }

        if (!accountDomain.isActiveAccount(accountObject)) {
            throw new BadRequestException("account is not activated");
        }

        return tokenAuthenticationService.generateLoginToken(accountId, expiresIn);
    }


}
