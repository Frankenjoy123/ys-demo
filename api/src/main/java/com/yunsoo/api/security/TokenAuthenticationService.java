package com.yunsoo.api.security;

import com.yunsoo.api.dto.basic.Token;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TAccountStatusEnum;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */

@Service
public class TokenAuthenticationService {

    @Value("${yunsoo.api.access_token.header_name}")
    private String ACCESS_TOKEN_HEADER_NAME;

    private final TokenHandler tokenHandler;

    public TokenAuthenticationService() {
        //load secret Key from properties file.
        tokenHandler = new TokenHandler();
    }


    public AccountAuthentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
        //support anonymous visit(for token is empty), or validate and parse from token
        TAccount tAccount = (token == null) ? new TAccount(TAccountStatusEnum.ANONYMOUS) : tokenHandler.parseAccessToken(token);
        return new AccountAuthentication(tAccount);
    }

    public AccountAuthentication getAuthentication() {
        return (AccountAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    //Mainly used by AuthController
    public Token generateAccessToken(String accountId, String orgId) {
        DateTime expires = DateTime.now().plusMinutes(60);
        return new Token(tokenHandler.createAccessToken(accountId, orgId, expires), expires);
    }

    //By pass the filter way, for each controller to check permission on their needs.
    public Boolean checkOrgResource(String token, String orgId, Boolean checkEqual) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        TAccount tAccount = tokenHandler.parseAccessToken(token);
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
