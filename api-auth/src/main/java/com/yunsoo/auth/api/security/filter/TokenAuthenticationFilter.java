package com.yunsoo.auth.api.security.filter;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.security.AccountAuthentication;
import com.yunsoo.auth.api.security.TokenAuthenticationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private final TokenAuthenticationService tokenAuthenticationService;

    public TokenAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // try get token from HttpHeader
        String token = ((HttpServletRequest) req).getHeader(Constants.HttpHeaderName.ACCESS_TOKEN);
        if (token == null) {
            token = req.getParameter("access_token"); // try get token from query string
        }

        if (token != null && token.length() > 0) {
            if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AccountAuthentication)) {
                final AccountAuthentication accountAuthentication = tokenAuthenticationService.getAuthentication(token);
                if (accountAuthentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(accountAuthentication);
                }
            }
        }

        chain.doFilter(req, res); // always continue
    }
}