package com.yunsoo.api.rabbit.security;

import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.api.rabbit.object.TAccountStatusEnum;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.joda.time.DateTime;
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

        if (token == null) {
            return null; //support anonymous visit
        }
        TAccount tAccount = tokenHandler.parseUserFromToken(token); //validate and parse from token
        //Invalid token
        if (tAccount == null) {
            tAccount = new TAccount();
            tAccount.setStatus(TAccountStatusEnum.UNDEFINED.value());
//            throw new UnauthorizedException(40101, "Account token is invalid!");
            // return null;  //403 forbidden
        } else if (tAccount.getExpires() < DateTime.now().getMillis()) {
            //expired - thrown exception!
            tAccount = new TAccount();
            tAccount.setStatus(TAccountStatusEnum.EXPIRED.value());
//            throw new UnauthorizedException(40102, "Account token is expired");
        }

        return new AccountAuthentication(tAccount);
    }

    //Mainly used by Auth/Login action.
    public String generateToken(TAccount account) {
        account.setExpires(System.currentTimeMillis() + HALF_YEAR);
        return tokenHandler.createTokenForUser(account);
    }
}
