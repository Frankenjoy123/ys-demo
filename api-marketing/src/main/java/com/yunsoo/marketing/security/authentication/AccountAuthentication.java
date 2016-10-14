package com.yunsoo.marketing.security.authentication;

import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import com.yunsoo.marketing.security.AuthAccount;
import com.yunsoo.marketing.security.AuthDetails;
import com.yunsoo.marketing.security.authorization.AuthorizationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-28
 * Descriptions:
 */
public class AccountAuthentication implements Authentication {

    private String credentials;
    private AuthDetails authDetails;
    private final AuthAccount authAccount;
    private AuthorizationService authorizationService;
    private boolean authenticated = true;

    public AccountAuthentication(AuthAccount authAccount, AuthorizationService authorizationService) {
        this.authAccount = authAccount;
        this.authorizationService = authorizationService;
    }

    public boolean checkPermission(RestrictionExpression restriction, PermissionExpression permission) {
        return authorizationService.checkPermission(restriction, permission);
    }

    public AccountAuthentication fillCredentials(String credentials) {
        this.credentials = credentials;
        return this;
    }

    public AccountAuthentication fillDetails(AuthDetails authDetails) {
        this.authDetails = authDetails;
        return this;
    }

    @Override
    public String getName() {
        return authAccount.getId();
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public AuthDetails getDetails() {
        return authDetails;
    }

    @Override
    public AuthAccount getPrincipal() {
        return authAccount;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException();
        }
        authenticated = false;
    }

}
