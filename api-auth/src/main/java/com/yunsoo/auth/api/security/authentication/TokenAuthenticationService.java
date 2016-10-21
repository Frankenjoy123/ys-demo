package com.yunsoo.auth.api.security.authentication;

import com.yunsoo.auth.api.security.authorization.AuthorizationService;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.common.web.security.authentication.AuthAccount;
import com.yunsoo.common.web.security.authentication.TokenHandler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public Token generateAccessToken(AuthAccount authAccount) {
        DateTime expires = DateTime.now().plusMinutes(accessTokenExpiresMinutes);
        String token = accessTokenHandler.createToken(expires, authAccount);
        return new Token(token, expires);
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

    public Token generateLoginToken(AuthAccount authAccount, Integer expiresIn) {
        DateTime expires;
        if (expiresIn == null || expiresIn <= 0) {
            expires = DateTime.now().plusMinutes(loginTokenExpiresMinutes);
        } else {
            expires = DateTime.now().plusSeconds(expiresIn);
        }
        return new Token(loginTokenHandler.createToken(expires, authAccount), expires);
    }

    public AuthAccount parseAccessToken(String token) {
        return accessTokenHandler.parseTokenAsAuthAccount(token);
    }

    public AuthAccount parseLoginToken(String token) {
        return loginTokenHandler.parseTokenAsAuthAccount(token);
    }

}
