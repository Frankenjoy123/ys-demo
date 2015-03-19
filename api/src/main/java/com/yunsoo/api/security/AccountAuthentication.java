package com.yunsoo.api.security;

import com.yunsoo.api.object.TAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Zhe on 2015/3/5.
 */
public class AccountAuthentication implements Authentication {

    private final TAccount tAccount;
    private boolean authenticated = true;

    public AccountAuthentication(TAccount user) {
        this.tAccount = user;
    }

    @Override
    public String getName() {
        return tAccount.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return tAccount.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return tAccount.getPassword();
    }

    @Override
    public TAccount getDetails() {
        return tAccount;
    }

    @Override
    public Object getPrincipal() {
        return tAccount.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}