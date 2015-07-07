package com.yunsoo.api.object;

import org.joda.time.DateTime;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * TAccount represents API user who consumes Yunsoo API.
 * Created by Zhe on 2015/3/5.
 */
public class TAccount implements UserDetails {
    private String id;
    private String orgId;
    private DateTime expires;
    private Set<TAccountAuthority> authorities;

    public TAccount() {
    }


    @Override
    public Set<TAccountAuthority> getAuthorities() {
        return authorities;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public DateTime getExpires() {
        return expires;
    }

    public void setExpires(DateTime expires) {
        this.expires = expires;
    }

}