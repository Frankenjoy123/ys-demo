package com.yunsoo.api.security.authentication;

import com.yunsoo.api.security.AuthDetails;
import com.yunsoo.api.security.authorization.AuthorizationService;
import com.yunsoo.api.security.authorization.PermissionGrantedAuthority;
import com.yunsoo.common.web.security.authentication.AuthAccount;
import com.yunsoo.common.web.security.permission.PermissionEntry;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<PermissionEntry> permissionEntries;
    private boolean authenticated = true;

    public AccountAuthentication(AuthAccount authAccount, AuthorizationService authorizationService) {
        this.authAccount = authAccount;
        this.authorizationService = authorizationService;
    }

    public List<PermissionEntry> getPermissionEntries() {
        if (permissionEntries == null) {
            permissionEntries = authorizationService.getPermissionEntries();
        }
        return permissionEntries;
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
    public List<PermissionGrantedAuthority> getAuthorities() {
        return getPermissionEntries().stream().map(PermissionGrantedAuthority::new).collect(Collectors.toList());
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
