package com.yunsoo.api.security;

import com.yunsoo.api.object.TAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */
public class AccountAuthentication implements Authentication {

    private final TAccount tAccount;
    private boolean authenticated = true;

    public AccountAuthentication(TAccount tAccount) {
        this.tAccount = tAccount;
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
    public TAccount getPrincipal() {
        return tAccount;
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
