package com.yunsoo.api.rabbit.security;

import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by Zhe on 2015/3/5.
 */

@Service
public class TokenAuthenticationService {

    @Value("${yunsoo.token_header_name}")
    private String AUTH_HEADER_NAME;
    private static final long HALF_YEAR = 1000 * 60 * 60 * 24 * 150; // 150 days

    private final TokenHandler tokenHandler;

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    public TokenAuthenticationService(@Value("${yunsoo.token_secret}") String secret) {
        //load secret Key from properties file.
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
    }

    public void addAuthentication(HttpServletResponse response, AccountAuthentication authentication) {
        final TAccount account = authentication.getDetails();
        account.setExpires(System.currentTimeMillis() + HALF_YEAR);
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(account));
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);

        if (token == null || token.equals("DENY")) {
            return null;
        }
        TAccount currentAccount = tokenHandler.parseUserFromToken(token); //validate and parse from token
        TAccount tAccount = dataAPIClient.get("account/token/{token}", TAccount.class, token);
        if (tAccount == null) {
            return null;
        }
        return new AccountAuthentication(tAccount);
    }

    public String generateToken(TAccount account) {
        return tokenHandler.createTokenForUser(account);
    }
}
