package com.yunsoo.api.rabbit.security;

import com.yunsoo.api.rabbit.object.TUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by:   Zhe
 * Created on:   2015/3/5
 * Descriptions:
 */
public class UserAuthentication implements Authentication {

    private final TUser tUser;
    private boolean authenticated = true;

    public UserAuthentication(TUser user) {
        this.tUser = user;
    }

    @Override
    public String getName() {
        return tUser.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return tUser.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return tUser.getPassword();
    }

    @Override
    public TUser getDetails() {
        return tUser;
    }

    @Override
    public Object getPrincipal() {
        return tUser.getUsername();
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