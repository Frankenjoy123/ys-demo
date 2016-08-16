package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.security.AuthAccount;
import com.yunsoo.auth.api.security.authentication.TokenAuthenticationService;
import com.yunsoo.auth.api.util.IpUtils;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.AccountLoginRequest;
import com.yunsoo.auth.dto.AccountLoginResponse;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.auth.service.AccountLoginLogService;
import com.yunsoo.auth.service.AccountTokenService;
import com.yunsoo.auth.service.LoginService;
import com.yunsoo.common.web.Constants;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by:   Lijian
 * Created on:   2016-07-05
 * Descriptions:
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private AccountTokenService accountTokenService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private AccountLoginLogService accountLoginLogService;

    /**
     * login with password
     *
     * @param appId        String
     * @param deviceId     String nullable
     * @param loginRequest AccountLoginRequest
     * @return AccountLoginResponse include permanentToken and accessToken
     */
    @RequestMapping(value = "password", method = RequestMethod.POST)
    public AccountLoginResponse login(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestBody @Valid AccountLoginRequest loginRequest,
            HttpServletRequest httpServletRequest) {

        log.info(String.format("password login request from [appId: %s, deviceId: %s]", appId, deviceId));

        //validate parameters
        if (!StringUtils.hasText(loginRequest.getAccountId())
                && (!StringUtils.hasText(loginRequest.getOrganization()) || !StringUtils.hasText(loginRequest.getIdentifier()))) {
            log.warn(String.format("password login request invalid [accountId: %s, organization: %s, identifier: %s]",
                    loginRequest.getAccountId(), loginRequest.getOrganization(), loginRequest.getIdentifier()));
            throw new BadRequestException();
        }

        //login
        Account account = loginService.login(loginRequest);
        if (account == null) {
            throw new UnauthorizedException("account is not valid");
        }

        //login successfully
        log.info(String.format("password login successfully, [id: %s, orgId: %s, identifier: %s, appId: %s, deviceId: %s]",
                account.getId(), account.getOrgId(), account.getIdentifier(), appId, deviceId));

        accountLoginLogService.savePasswordLogin(account.getId(), appId, deviceId, IpUtils.getIpFromRequest(httpServletRequest), userAgent);

        return createResponse(account.getId(), account.getOrgId(), appId, deviceId);
    }

    /**
     * login with loginToken
     *
     * @param appId      String
     * @param deviceId   String nullable
     * @param loginToken Token
     * @return AccountLoginResponse include permanentToken and accessToken
     */
    @RequestMapping(value = "token", method = RequestMethod.POST)
    public AccountLoginResponse login(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestBody Token loginToken,
            HttpServletRequest httpServletRequest) {

        log.info(String.format("token login request from [appId: %s, deviceId: %s]", appId, deviceId));

        //validate login token
        if (StringUtils.isEmpty(loginToken.getToken())) {
            throw new BadRequestException();
        }

        //parse login token
        AuthAccount authAccount = tokenAuthenticationService.parseLoginToken(loginToken.getToken());
        if (authAccount == null || StringUtils.isEmpty(authAccount.getId())) {
            log.warn(String.format("token is not valid [token: %s]", loginToken.getToken()));
            throw new UnauthorizedException("token is not valid");
        }

        //login
        Account account = loginService.login(authAccount.getId());
        if (account == null) {
            throw new UnauthorizedException("account is not valid");
        }

        //login successfully
        log.info(String.format("token login successfully, [id: %s, orgId: %s, identifier: %s, appId: %s, deviceId: %s]",
                account.getId(), account.getOrgId(), account.getIdentifier(), appId, deviceId));

        accountLoginLogService.saveTokenLogin(account.getId(), appId, deviceId, IpUtils.getIpFromRequest(httpServletRequest), userAgent);

        return createResponse(account.getId(), account.getOrgId(), appId, deviceId);
    }

    private AccountLoginResponse createResponse(String accountId, String orgId, String appId, String deviceId) {
        Token permanentToken = accountTokenService.createPermanentToken(accountId, appId, deviceId);
        Token accessToken = tokenAuthenticationService.generateAccessToken(accountId, orgId);
        return new AccountLoginResponse(permanentToken, accessToken);
    }

}
