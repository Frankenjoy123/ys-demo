package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.security.authentication.TokenAuthenticationService;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.*;
import com.yunsoo.auth.service.AccountService;
import com.yunsoo.auth.service.LoginService;
import com.yunsoo.auth.service.OAuthAccountService;
import com.yunsoo.auth.service.WeChatService;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import com.yunsoo.common.web.security.authentication.AuthAccount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by:   Lijian
 * Created on:   2016-10-18
 * Descriptions:
 */
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private static int LOGIN_TOKEN_EXPIRES_SECONDS = 1440;
    private static String SOURCE = "source";
    private static String SOURCE_TYPE = "source_type_code";
    private static String WECHAT="wechat";

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private OAuthAccountService oAuthAccountService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WeChatService weChatService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public OAuthAccountLoginResponse login(@RequestBody OAuthAccountLoginRequest request) {
        if(request.getOauthOpenType() == null)
            request.setOauthOpenType(WECHAT);
        List<OAuthAccount> accountList = oAuthAccountService.getByOAuthOpenIdAndOAuthTypeCode(request.getOauthOpenid(), request.getOauthOpenType());
        if(accountList.size() ==0)
            throw new UnauthorizedException("wechat user not bind");

        OAuthAccount account = accountList.get(0);
        Token accessToken = getAccessToken(account.getId(), account.getToken());

        OAuthAccountLoginResponse response = new OAuthAccountLoginResponse();
        response.setToken(account.getToken());
        response.setAccessToken(accessToken);
        response.setOauthAccountId(account.getId());
        return response;
    }

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public OAuthAccountLoginResponse bind(@RequestBody OAuthAccountLoginRequest request) {
        if(request.getOauthOpenType() == null)
            request.setOauthOpenType(WECHAT);

        AuthAccount account = tokenAuthenticationService.parseLoginToken(request.getLoginToken());
        if (account == null || StringUtils.isEmpty(account.getId())) {
            log.warn(String.format("login token is not valid [token: %s]", request.getLoginToken()));
            throw new UnauthorizedException("login token is not valid");
        }

        //login
        Account userAccount = loginService.login(account.getId());
        if (userAccount == null) {
            throw new UnauthorizedException("account is not valid");
        }

        List<OAuthAccount> accountList = oAuthAccountService.getByOAuthOpenIdAndOAuthTypeCode(request.getOauthOpenid(), request.getOauthOpenType());
        OAuthAccount currentAccount;
        if(accountList.size() ==0) {

            OAuthAccount oAuthAccount = new OAuthAccount();
            oAuthAccount.setCreatedDateTime(DateTime.now());
            oAuthAccount.setAccountId(account.getId());
            oAuthAccount.setToken(HashUtils.sha1HexString(UUID.randomUUID().toString()));  //random sha1

            if(account.getDetails()!=null) {
                oAuthAccount.setSource(account.getDetails().get(SOURCE));
                oAuthAccount.setSourceTypeCode(account.getDetails().get(SOURCE_TYPE));
            }
            oAuthAccount.setDisabled(false);
            oAuthAccount.setoAuthTypeCode(request.getOauthOpenType());

            if (request.getOauthOpenType().equals(WECHAT)) {
                WeChatUser weChatUser = weChatService.getUserInfo(request.getOauthToken(), request.getOauthOpenid());
                if (StringUtils.hasText(weChatUser.getErrorCode()))
                    throw new BadRequestException("could not get wechat user, error message: " + weChatUser.getErrorMsg() + ", error code: " + weChatUser.getErrorCode());


                oAuthAccount.setGravatarUrl(weChatUser.getImageUrl());
                oAuthAccount.setName(weChatUser.getNickName());
                oAuthAccount.setoAuthOpenId(weChatUser.getOpenId());
            }

            currentAccount = oAuthAccountService.save(oAuthAccount);
        }
        else
            currentAccount = accountList.get(0);

        OAuthAccountLoginResponse response = new OAuthAccountLoginResponse();
        response.setAccessToken(tokenAuthenticationService.generateAccessToken(account));
        response.setToken(currentAccount.getToken());
        response.setOauthAccountId(currentAccount.getId());

        return response;
    }

    @RequestMapping(value = "/loginToken", method = RequestMethod.GET)
    public Token getLoginToken(@RequestParam(value = "source_type_code", required = false) String sourceTypeCode,
                               @RequestParam(value = "source", required = false) String source) {
        if(sourceTypeCode == null)
            sourceTypeCode = "agency";

        String currentAccountId = AuthUtils.getCurrentAccount().getId();

        List<Account> agencyAccountList = accountService.getByTypeCode(Constants.AccountType.AGENCY, AuthUtils.fixOrgId(null));
        if(agencyAccountList.size()==0)
            throw new BadRequestException("no agency account exists");
        String accountId = agencyAccountList.get(0).getId();

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
        if (sourceTypeCode == null || (sourceTypeCode != null && sourceTypeCode.equals("agency"))) {
            authAccount.setDetails(new HashMap<>());
            authAccount.getDetails().put(SOURCE_TYPE, sourceTypeCode);
            authAccount.getDetails().put(SOURCE, source);
        }
        return tokenAuthenticationService.generateLoginToken(authAccount, LOGIN_TOKEN_EXPIRES_SECONDS);
    }

    /**
     * get access token with id and token in oauth_account
     *
     * @param oAuthAccountId
     * @param token
     * @return
     */
    @RequestMapping(value = "accessToken", method = RequestMethod.GET)
    public Token getAccessToken(@RequestParam("oauth_account_id") String oAuthAccountId,
                                @RequestParam("token") String token) {

        OAuthAccount account = oAuthAccountService.getById(oAuthAccountId);
        if (account == null)
            throw new BadRequestException("oauth account not existed");
        if (!account.getToken().equals(token))
            throw new BadRequestException("token is invalid");
        if (account.getDisabled())
            throw new BadRequestException("oauth account is invalid");

        Account userAccount = loginService.login(account.getAccountId());
        if (userAccount == null)
            throw new BadRequestException("account is invalid");

        AuthAccount authAccount = new AuthAccount();
        authAccount.setId(account.getAccountId());
        authAccount.setOrgId(userAccount.getOrgId());
        authAccount.setDetails(new HashMap<>());
        authAccount.getDetails().put(SOURCE_TYPE, account.getSourceTypeCode());
        authAccount.getDetails().put(SOURCE, account.getSource());

        return tokenAuthenticationService.generateAccessToken(authAccount);
    }


}
