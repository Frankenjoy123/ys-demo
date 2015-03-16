package com.yunsoo.api.security;

import com.yunsoo.api.dto.basic.Account;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by Zhe on 2015/3/5.
 */

@Service
public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

    private final TokenHandler tokenHandler;

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    public TokenAuthenticationService(@Value("${yunsoo.token.secret}") String secret) {
        //load secret Key from properties file.
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
    }

    public void addAuthentication(HttpServletResponse response, AccountAuthentication authentication) {
        final TAccount account = authentication.getDetails();
        //account.setExpires(System.currentTimeMillis() + TEN_DAYS);
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(account));
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);

        if (token == null || token.equals("DENY")) {
            return null;
        }
        TAccount tAccount = dataAPIClient.get("account/token/{token}", TAccount.class, token);
        if (tAccount == null) {
            return null;
        }
        return new AccountAuthentication(tAccount);
    }
}
