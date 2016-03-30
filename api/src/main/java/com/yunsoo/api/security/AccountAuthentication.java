package com.yunsoo.api.security;

import com.yunsoo.api.security.authorization.AuthorizationService;
import com.yunsoo.api.security.authorization.PermissionGrantedAuthority;
import com.yunsoo.api.security.permission.PermissionEntry;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */
public class AccountAuthentication implements Authentication {

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
            permissionEntries = authorizationService.getPermissionEntries(authAccount.getId());
        }
        return permissionEntries;
    }

    public boolean checkPermission(RestrictionExpression restriction, PermissionExpression permission) {
        return authorizationService.checkPermission(getPermissionEntries(), restriction, permission);
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
    public Object getCredentials() {
        return null;
    }

    @Override
    public AuthAccount getDetails() {
        return authAccount;
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
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException();
        }
        authenticated = false;
    }
}
