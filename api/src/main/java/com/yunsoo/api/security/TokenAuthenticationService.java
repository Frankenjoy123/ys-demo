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

    private final TokenHandler tokenHandler;

    public TokenAuthenticationService() {
        //load secret Key from properties file.
        this.tokenHandler = new TokenHandler();
    }


    public AccountAuthentication getAuthentication(String token) {
        if (token == null) {
            return null;
        }
        TAccount tAccount = tokenHandler.parseAccessToken(token);
        if (tAccount == null) {
            return null;
        }
        return new AccountAuthentication(tAccount);
    }

    public AccountAuthentication getAuthentication() {
        return (AccountAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    //Used by AuthController
    public Token generateAccessToken(String accountId, String orgId) {
        DateTime expires = DateTime.now().plusMinutes(accessTokenExpiresMinutes);
        return new Token(tokenHandler.createAccessToken(accountId, orgId, expires), expires);
    }

}
