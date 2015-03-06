package com.yunsoo.api.object;

/**
 * Represent roles.
 * Created by Zhe on 2015/3/5.
 */
public enum TAccountRole {
    COM_USER, //Company user
    YUNSOO_ADMIN;  //Yunsoo platform admin

    public TAccountAuthority asAuthorityFor(final TAccount user) {
        final TAccountAuthority authority = new TAccountAuthority();
        authority.setAuthority(this.toString());
        authority.setAccount(user);
        return authority;
    }

    public static TAccountRole valueOf(final TAccountAuthority authority) {
        switch (authority.getAuthority()) {
            case "COM_USER":
                return COM_USER;
            case "YUNSOO_ADMIN":
                return YUNSOO_ADMIN;
        }
        throw new IllegalArgumentException("No role defined for authority: " + authority.getAuthority());
    }
}
