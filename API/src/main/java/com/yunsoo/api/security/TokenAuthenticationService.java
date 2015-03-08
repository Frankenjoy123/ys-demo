package com.yunsoo.api.security;

import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TAccountRole;
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
    public TokenAuthenticationService(@Value("${yunsoo.token.secret}") String secret) {
        //load secret Key from properties file.
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
    }

    public void addAuthentication(HttpServletResponse response, AccountAuthentication authentication) {
        final TAccount account = authentication.getDetails();
        account.setExpires(System.currentTimeMillis() + TEN_DAYS);
        response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(account));
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);

        //mock-up, to be updated by Kaibing
        if (token == null || !token.equals("DENY")) {
            final TAccount tAccount = new TAccount();
            tAccount.setUsername("YunsooAdmin");
            tAccount.setPassword(new BCryptPasswordEncoder().encode("12345678"));
            tAccount.grantRole(TAccountRole.YUNSOO_ADMIN);
            if (tAccount != null) {
                return new AccountAuthentication(tAccount);
            }
        } else {
            return null;
        }

        //to-be
//        if (token != null) {
//            final TAccount account = tokenHandler.parseUserFromToken(token);
//            if (account != null) {
//                return new AccountAuthentication(account);
//            }
//        }
        return null;
    }
}
