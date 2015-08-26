package com.yunsoo.api.rabbit.security;

import com.yunsoo.api.rabbit.dto.Token;
import com.yunsoo.api.rabbit.object.TUser;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by:   Zhe
 * Created on:   2015/3/5
 * Descriptions:
 */
@Service
public class TokenAuthenticationService {

    @Value("${yunsoo.access_token.expires_minutes}")
    private int accessTokenExpiresMinutes;

    private final TokenHandler accessTokenHandler;


    @Autowired
    public TokenAuthenticationService(@Value("${yunsoo.access_token.hash_salt}") String accessTokenHashSalt) {
        accessTokenHandler = new TokenHandler(accessTokenHashSalt);
    }

    public UserAuthentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UserAuthentication)) {
            throw new UnauthorizedException();
        }
        return (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public UserAuthentication getAuthentication(String token) {
        if (token == null) {
            return null;
        }
        TUser tUser = accessTokenHandler.parseToken(token);
        if (tUser == null) {
            return null;
        }
        return new UserAuthentication(tUser);
    }

    public Token generateAccessToken(String userId) {
        DateTime expires = DateTime.now().plusMinutes(accessTokenExpiresMinutes);
        return new Token(accessTokenHandler.createToken(expires, userId), expires);
    }


//    private static final long HALF_YEAR = 1000000 * 60 * 60 * 24 * 150; // 150 days
//    private static final long TEN_YEARS = 1000000 * 60 * 60 * 24 * 365 * 10; // 10 years

//
//    public void addAuthentication(HttpServletResponse response, AccountAuthentication authentication) {
//        final TUser account = authentication.getDetails();
//        account.setExpires(System.currentTimeMillis() + HALF_YEAR);
//        response.addHeader(Constants.HttpHeaderName.ACCESS_TOKEN, tokenHandler.createTokenForUser(account));
//    }

//    public Authentication getAuthentication(HttpServletRequest request) {
//        final String token = request.getHeader(Constants.HttpHeaderName.ACCESS_TOKEN);
//        //support anonymous visit(for token is empty), or validate and parse from token
//        TUser tUser = (token == null) ? tUser = new TUser(TAccountStatusEnum.ANONYMOUS) : tokenHandler.parseUserFromToken(token);
//        return new AccountAuthentication(tUser);
//    }
//
//    //Mainly used by Auth/Login action.
//    public String generateToken(TUser account, Boolean stayLong) {
//        long tokenTimeSpan = stayLong ? TEN_YEARS : HALF_YEAR;  //anonymous user got ten years' token.
//        account.setExpires(System.currentTimeMillis() + tokenTimeSpan);
//        return tokenHandler.createTokenForUser(account);
//    }
//
//    public TUser parseUser(String token) {
//        return tokenHandler.parseUserFromToken(token);
//    }

    //By pass the filter way, for each controller to check permission on their needs.
//    public Boolean checkIdentity(String token, String userId, Boolean checkEqual) {
//        if (token == null || token.isEmpty()) {
//            return false;
//        }
//
//        TUser tUser = tokenHandler.parseUserFromToken(token);
//        if (!tUser.isAnonymous()) {
//            throw new ForbiddenException(40101, "Anonymous user is denied!");
//        } else if (!tUser.isAccountNonExpired()) {
//            throw new UnauthorizedException(40102, "Account is expired");
//        } else if (!tUser.isAccountNonLocked()) {
//            throw new UnauthorizedException(40103, "Account is locked!");
//        } else if (tUser.isCredentialsInvalid()) {
//            throw new UnauthorizedException(40104, "Account token is invalid!");
//        } else if (!tUser.isEnabled()) {
//            throw new UnauthorizedException(40105, "Account is disabled!");
//        }
//
//        return checkEqual ? tUser.getId().equals(userId) : true;
//    }
}
