package com.yunsoo.api.security;

import com.yunsoo.api.dto.Token;
import com.yunsoo.api.security.authorization.AuthorizationService;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
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


    public AccountAuthentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AccountAuthentication)) {
            throw new UnauthorizedException();
        }
        return (AccountAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public AccountAuthentication getAuthentication(String token) {
        if (token == null) {
            return null;
        }
        AuthAccount authAccount = accessTokenHandler.parseToken(token);
        if (authAccount == null) {
            return null;
        }
        return new AccountAuthentication(authAccount, authorizationService);
    }

    public AuthAccount parseAccessToken(String token) {
        return accessTokenHandler.parseToken(token);
    }

    public AuthAccount parseLoginToken(String token) {
        return loginTokenHandler.parseToken(token);
    }

    public Token generateAccessToken(String accountId, String orgId) {
        DateTime expires = DateTime.now().plusMinutes(accessTokenExpiresMinutes);
        return new Token(accessTokenHandler.createToken(expires, accountId, orgId), expires);
    }

    public Token generateLoginToken(String accountId) {
        DateTime expires = DateTime.now().plusMinutes(loginTokenExpiresMinutes);
        return new Token(loginTokenHandler.createToken(expires, accountId), expires);
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
