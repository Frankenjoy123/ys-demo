package com.yunsoo.api.security;

import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TAccountStatusEnum;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ForbiddenException;
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
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */

@Service
public class TokenAuthenticationService {

    @Value("${yunsoo.token_header_name}")
    private String AUTH_HEADER_NAME;
    private static final long HALF_YEAR = 1000000L * 60 * 60 * 24 * 150; // 150 days

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
        //support anonymous visit(for token is empty), or validate and parse from token
        TAccount tAccount = (token == null) ? tAccount = new TAccount(TAccountStatusEnum.ANONYMOUS) : tokenHandler.parseUserFromToken(token);
        return new AccountAuthentication(tAccount);
    }

    //Mainly used by Auth/Login action.
    public String generateToken(TAccount account) {
        account.setExpires(System.currentTimeMillis() + HALF_YEAR);
        return tokenHandler.createTokenForUser(account);
    }

    //By pass the filter way, for each controller to check permission on their needs.
    public Boolean checkOrgResource(String token, String orgId, Boolean checkEqual) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        TAccount tAccount = tokenHandler.parseUserFromToken(token);
        if (!tAccount.isAnonymous()) {
            throw new ForbiddenException(40101, "Action placeholder", "Anonymous user is denied!");
        } else if (!tAccount.isAccountNonExpired()) {
            throw new UnauthorizedException(40102, "Account is expired");
        } else if (!tAccount.isAccountNonLocked()) {
            throw new UnauthorizedException(40103, "Account is locked!");
        } else if (tAccount.isCredentialsInvalid()) {
            throw new UnauthorizedException(40104, "Account token is invalid!");
        } else if (!tAccount.isEnabled()) {
            throw new UnauthorizedException(40105, "Account is disabled!");
        }

        return checkEqual ? tAccount.getOrgId().equals(orgId) : true;
    }
}
