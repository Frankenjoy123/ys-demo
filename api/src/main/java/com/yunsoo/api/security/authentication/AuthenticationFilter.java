package com.yunsoo.api.security.authentication;

import com.yunsoo.api.security.AuthDetails;
import com.yunsoo.common.web.Constants;
import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-07-28
 * Descriptions:
 */
public class AuthenticationFilter extends GenericFilterBean {

    private final TokenAuthenticationService tokenAuthenticationService;

    public AuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // try get token from HttpHeader
        HttpServletRequest request = ((HttpServletRequest) req);
        String token = request.getHeader(Constants.HttpHeaderName.ACCESS_TOKEN);
        if (token == null) {
            token = req.getParameter("access_token"); // try get token from query string
        }

        if (token != null && token.length() > 0) {
            if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AccountAuthentication)) {
                final AccountAuthentication accountAuthentication = tokenAuthenticationService.getAuthentication(token);
                if (accountAuthentication != null) {
                    //fill details
                    accountAuthentication.fillDetails(getAuthDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(accountAuthentication);
                }
            }
        }

        chain.doFilter(req, res); // always continue
    }

    private AuthDetails getAuthDetails(HttpServletRequest request) {
        String appId = request.getHeader(Constants.HttpHeaderName.APP_ID);
        String deviceId = request.getHeader(Constants.HttpHeaderName.DEVICE_ID);
        if (appId == null) {
            appId = request.getParameter("app_id"); // try get app_id from query string
        }
        if (deviceId == null) {
            deviceId = request.getParameter("device_id"); // try get device_id from query string
        }
        if (appId != null && appId.length() > 19) {
            throw new BadRequestException("app_id invalid");
        }
        if (deviceId != null && deviceId.length() > 40) {
            throw new BadRequestException("device_id invalid");
        }
        AuthDetails authDetails = new AuthDetails();
        authDetails.setAppId(appId);
        authDetails.setDeviceId(deviceId);
        return authDetails;
    }
}