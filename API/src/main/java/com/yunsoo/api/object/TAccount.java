package com.yunsoo.api.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * TAccount represents API user who consumes Yunsoo API.
 * Created by Zhe on 2015/3/5.
 */
public class TAccount implements UserDetails {

    public TAccount() {
    }

    public TAccount(String username) {
        this.username = username;
    }

    public TAccount(String username, Date expires) {
        this.username = username;
        this.expires = expires.getTime();
    }

    private Long id;

    @NotNull
    @Size(min = 4, max = 30)
    private String username;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    //    @Transient
    private long expires;

    @NotNull
    private boolean accountExpired;

    @NotNull
    private boolean accountLocked;

    @NotNull
    private boolean credentialsExpired;

    @NotNull
    private boolean accountEnabled;

    //    @Transient
    private String newPassword;

    private Set<TAccountAuthority> authorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getNewPassword() {
        return newPassword;
    }

    @JsonProperty
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    @JsonIgnore
    public Set<TAccountAuthority> getAuthorities() {
        return authorities;
    }

    // Use Roles as external API
    public Set<TAccountRole> getRoles() {
        Set<TAccountRole> roles = EnumSet.noneOf(TAccountRole.class);
        if (authorities != null) {
            for (TAccountAuthority authority : authorities) {
                roles.add(TAccountRole.valueOf(authority));
            }
        }
        return roles;
    }

    public void setRoles(Set<TAccountRole> roles) {
        for (TAccountRole role : roles) {
            grantRole(role);
        }
    }

    public void grantRole(TAccountRole role) {
        if (authorities == null) {
            authorities = new HashSet<TAccountAuthority>();
        }
        authorities.add(role.asAuthorityFor(this));
    }

    public void revokeRole(TAccountRole role) {
        if (authorities != null) {
            authorities.remove(role.asAuthorityFor(this));
        }
    }

    public boolean hasRole(TAccountRole role) {
        return authorities.contains(role.asAuthorityFor(this));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return !accountEnabled;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getUsername();
    }
}