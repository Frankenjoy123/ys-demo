package com.yunsoo.auth.api.security.authentication;

import com.yunsoo.auth.api.security.AuthAccount;
import com.yunsoo.auth.api.security.authorization.AuthorizationService;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.common.web.security.authentication.TokenHandler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Created by:   Lijian
 * Created on:   2016-07-05
 * Descriptions:
 */
@Service
public class TokenAuthenticationService {

    @Value("${yunsoo.access_token.expires_minutes}")
    private int accessTokenExpiresMinutes;

    @Value("${yunsoo.access_token.hash_salt}")
    private String accessTokenHashSalt;

    @Value("${yunsoo.login_token.expires_minutes}")
    private int loginTokenExpiresMinutes;

    @Value("${yunsoo.login_token.hash_salt}")
    private String loginTokenHashSalt;

    private TokenHandler accessTokenHandler;

    private TokenHandler loginTokenHandler;

    @Autowired
    private AuthorizationService authorizationService;


    @PostConstruct
    public void init() {
        this.accessTokenHandler = new TokenHandler(accessTokenHashSalt);
        this.loginTokenHandler = new TokenHandler(loginTokenHashSalt);
    }

    public AccountAuthentication getAuthentication(String token) {
        if (token == null) {
            return null;
        }
        AuthAccount authAccount = parseAccessToken(token);
        if (authAccount == null) {
            return null;
        }
        return new AccountAuthentication(authAccount, authorizationService).fillCredentials(token);
    }

    public Token generateAccessToken(String accountId, String orgId) {
        DateTime expires = DateTime.now().plusMinutes(accessTokenExpiresMinutes);
        String token = accessTokenHandler.createToken(expires, accountId, orgId);
        return new Token(token, expires);
    }

    public Token generateAccessToken(String accountId, String orgId, String oAuthAccountId) {
        DateTime expires = DateTime.now().plusMinutes(accessTokenExpiresMinutes);
        String token = accessTokenHandler.createToken(expires, accountId, orgId, oAuthAccountId);
        return new Token(token, expires);
    }

    public AuthAccount parseAccessToken(String token) {
        if (token == null) {
            return null;
        }
        String[] values = accessTokenHandler.parseToken(token);
        if (values == null || values.length == 0) {
            return null;
        } else {
            AuthAccount authAccount = new AuthAccount();
            authAccount.setId(values[0]);
            if (values.length > 1 && StringUtils.hasText(values[1])) {
                authAccount.setOrgId(values[1]);
            }
            if (values.length > 2 && StringUtils.hasText(values[2])) {
                authAccount.setOAuthAccountId(values[2]);
            }
            return authAccount;
        }
    }

    /**
     * @param accountId String
     * @param expiresIn Integer, seconds
     * @return Token
     */
    public Token generateLoginToken(String accountId, Integer expiresIn) {
        DateTime expires;
        if (expiresIn == null || expiresIn <= 0) {
            expires = DateTime.now().plusMinutes(loginTokenExpiresMinutes);
        } else {
            expires = DateTime.now().plusSeconds(expiresIn);
        }
        return new Token(loginTokenHandler.createToken(expires, accountId), expires);
    }

    public Token generateLoginToken() {

        return null;
    }

    public AuthAccount parseLoginToken(String token) {
        if (token == null) {
            return null;
        }
        String[] values = loginTokenHandler.parseToken(token);
        if (values == null || values.length == 0) {
            return null;
        } else {
            AuthAccount authAccount = new AuthAccount();
            authAccount.setId(values[0]);
            if (values.length > 1 && StringUtils.hasText(values[1])) {
                authAccount.setOrgId(values[1]);
            }
            if (values.length > 2 && StringUtils.hasText(values[2])) {
                authAccount.setOAuthAccountId(values[2]);
            }
            return authAccount;
        }
    }

}
