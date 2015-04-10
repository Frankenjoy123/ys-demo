package com.yunsoo.api.object;

import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotNull;

/**
 * Represent Authorities of an account.
 * Created by Zhe on 2015/3/5.
 */
public class TAccountAuthority implements GrantedAuthority {

    @NotNull
    private TAccount account;

    @NotNull
    private String authority;

    public TAccount getAccount() {
        return account;
    }

    public void setAccount(TAccount account) {
        this.account = account;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TAccountAuthority))
            return false;

        TAccountAuthority ua = (TAccountAuthority) obj;
        return ua.getAuthority() == this.getAuthority() || ua.getAuthority().equals(this.getAuthority());
    }

    @Override
    public int hashCode() {
        return getAuthority() == null ? 0 : getAuthority().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getAuthority();
    }
}
