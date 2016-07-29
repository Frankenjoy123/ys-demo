package com.yunsoo.auth.api.security.authentication;

import com.yunsoo.auth.api.security.AuthAccount;
import com.yunsoo.auth.api.security.authorization.AuthorizationService;
import com.yunsoo.auth.dto.Token;
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
        AuthAccount authAccount = accessTokenHandler.parseToken(token);
        if (authAccount == null) {
            return null;
        }
        return new AccountAuthentication(authAccount, authorizationService).fillCredentials(token);
    }

    public AuthAccount parseAccessToken(String token) {
        if (token == null) {
            return null;
        }
        return accessTokenHandler.parseToken(token);
    }

    public AuthAccount parseLoginToken(String token) {
        if (token == null) {
            return null;
        }
        return loginTokenHandler.parseToken(token);
    }

    public Token generateAccessToken(String accountId, String orgId) {
        DateTime expires = DateTime.now().plusMinutes(accessTokenExpiresMinutes);
        return new Token(accessTokenHandler.createToken(expires, accountId, orgId), expires);
    }

    public Token generateLoginToken(String accountId) {
        return generateLoginToken(accountId, null);
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

}
