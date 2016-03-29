package com.yunsoo.api.security.authorization;

import com.yunsoo.api.security.permission.PermissionEntry;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by:   Lijian
 * Created on:   2016-03-28
 * Descriptions:
 */
public class AccountGrantedAuthority implements GrantedAuthority {

    private PermissionEntry permissionEntry;

    public AccountGrantedAuthority(String permissionEntry) {
        this.permissionEntry = PermissionEntry.parse(permissionEntry);
    }

    public AccountGrantedAuthority(PermissionEntry permissionEntry) {
        this.permissionEntry = permissionEntry;
    }


    @Override
    public String getAuthority() {
        return permissionEntry != null ? permissionEntry.toString() : null;
    }

    public PermissionEntry.Effect check(RestrictionExpression restriction, PermissionExpression permission) {
        return permissionEntry == null ? null : permissionEntry.check(restriction, permission);
    }
}
