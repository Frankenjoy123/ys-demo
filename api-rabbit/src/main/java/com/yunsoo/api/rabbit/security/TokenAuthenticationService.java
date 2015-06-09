package com.yunsoo.api.rabbit.security;

import com.yunsoo.api.rabbit.object.Constants;
import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.api.rabbit.object.TAccountStatusEnum;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.UnauthorizedException;
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

    //    @Value("${yunsoo.token_header_name}")
//    private String AUTH_HEADER_NAME;
    private static final long HALF_YEAR = 1000000 * 60 * 60 * 24 * 150; // 150 days
    private static final long TEN_YEARS = 1000000 * 60 * 60 * 24 * 365 * 10; // 10 years

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
        response.addHeader(Constants.HttpHeaderName.ACCESS_TOKEN, tokenHandler.createTokenForUser(account));
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(Constants.HttpHeaderName.ACCESS_TOKEN);
        //support anonymous visit(for token is empty), or validate and parse from token
        TAccount tAccount = (token == null) ? tAccount = new TAccount(TAccountStatusEnum.ANONYMOUS) : tokenHandler.parseUserFromToken(token);
        return new AccountAuthentication(tAccount);
    }

    //Mainly used by Auth/Login action.
    public String generateToken(TAccount account, Boolean stayLong) {
        long tokenTimeSpan = stayLong ? TEN_YEARS : HALF_YEAR;  //anonymous user got ten years' token.
        account.setExpires(System.currentTimeMillis() + tokenTimeSpan);
        return tokenHandler.createTokenForUser(account);
    }

    public TAccount parseUser(String token) {
        return tokenHandler.parseUserFromToken(token);
    }

    //By pass the filter way, for each controller to check permission on their needs.
    public Boolean checkIdentity(String token, String userId, Boolean checkEqual) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        TAccount tAccount = tokenHandler.parseUserFromToken(token);
        if (!tAccount.isAnonymous()) {
            throw new ForbiddenException(40101, "Anonymous user is denied!");
        } else if (!tAccount.isAccountNonExpired()) {
            throw new UnauthorizedException(40102, "Account is expired");
        } else if (!tAccount.isAccountNonLocked()) {
            throw new UnauthorizedException(40103, "Account is locked!");
        } else if (tAccount.isCredentialsInvalid()) {
            throw new UnauthorizedException(40104, "Account token is invalid!");
        } else if (!tAccount.isEnabled()) {
            throw new UnauthorizedException(40105, "Account is disabled!");
        }

        return checkEqual ? tAccount.getId().equals(userId) : true;
    }
}
