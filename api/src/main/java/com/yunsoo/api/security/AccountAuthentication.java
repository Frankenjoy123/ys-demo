package com.yunsoo.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/5
 * Descriptions:
 */
public class AccountAuthentication implements Authentication {

    private final AuthAccount authAccount;
    private boolean authenticated = true;

    public AccountAuthentication(AuthAccount authAccount) {
        this.authAccount = authAccount;
    }

    @Override
    public String getName() {
        return authAccount.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
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
