package com.yunsoo.api.security;

import com.yunsoo.api.security.authorization.AccountGrantedAuthority;
import com.yunsoo.api.security.authorization.AuthorizationService;
import com.yunsoo.api.security.permission.PermissionEntry;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */
public class AccountAuthentication implements Authentication {

    private final AuthAccount authAccount;
    private AuthorizationService authorizationService;
    private List<AccountGrantedAuthority> grantedAuthorities;
    private boolean authenticated = true;

    public AccountAuthentication(AuthAccount authAccount, AuthorizationService authorizationService) {
        this.authAccount = authAccount;
        this.authorizationService = authorizationService;
    }

    @Override
    public String getName() {
        return authAccount.getId();
    }

    @Override
    public List<AccountGrantedAuthority> getAuthorities() {
        if (grantedAuthorities == null) {
            grantedAuthorities = authorizationService.getGrantedAuthorities(authAccount.getId());
        }
        return grantedAuthorities;
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

    public boolean checkPermission(String restriction, String permission) {
        RestrictionExpression rExp = new RestrictionExpression.OrgRestrictionExpression(restriction);
        PermissionExpression pExp = new PermissionExpression.SimplePermissionExpression(permission);
        for (AccountGrantedAuthority grantedAuthority : getAuthorities()) {
            PermissionEntry.Effect effect = grantedAuthority.check(rExp, pExp);
            if (effect != null) {
                return effect.equals(PermissionEntry.Effect.allow);
            }
        }
        return false;
    }

}
