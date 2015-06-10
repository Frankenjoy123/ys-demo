package com.yunsoo.api.security;

import com.yunsoo.api.dto.Token;
import com.yunsoo.api.object.TAccount;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */

@Service
public class TokenAuthenticationService {

    @Value("${yunsoo.api.access_token.expires_minutes}")
    private int accessTokenExpiresMinutes;

    @Value("${yunsoo.api.login_token.expires_minutes}")
    private int loginTokenExpiresMinutes;

    private String accessTokenHashSalt = "lvtHDkfIUxJ2bLWHc0MNztUqCJSVPSJO";

    private String loginTokenHashSalt = "nC9UYPUupMaViIG3UBODY7B19IYW6Z0X";


    private TokenHandler accessTokenHandler;

    private TokenHandler loginTokenHandler;

    public TokenAuthenticationService() {
        this.accessTokenHandler = new TokenHandler(accessTokenHashSalt);
        this.loginTokenHandler = new TokenHandler(loginTokenHashSalt);
    }


    public AccountAuthentication getAuthentication() {
        return (AccountAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public AccountAuthentication getAuthentication(String token) {
        if (token == null) {
            return null;
        }
        TAccount tAccount = accessTokenHandler.parseToken(token);
        if (tAccount == null) {
            return null;
        }
        return new AccountAuthentication(tAccount);
    }

    public TAccount parseAccessToken(String token) {
        return accessTokenHandler.parseToken(token);
    }

    public TAccount parseLoginToken(String token) {
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

}
