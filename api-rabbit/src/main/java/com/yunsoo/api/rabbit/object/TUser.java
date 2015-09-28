package com.yunsoo.api.rabbit.object;

import org.joda.time.DateTime;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * Created by:   Zhe
 * Created on:   2015/3/5
 * Descriptions:
 */
public class TUser implements UserDetails {
    private String id;
    private DateTime expires;

    @Override
    public Set<TAccountAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return expires.isAfterNow();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getExpires() {
        return expires;
    }

    public void setExpires(DateTime expires) {
        this.expires = expires;
    }

}