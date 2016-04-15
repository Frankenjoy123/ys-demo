package com.yunsoo.api.security.authorization;

import com.yunsoo.api.security.permission.PermissionEntry;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by:   Lijian
 * Created on:   2016-03-28
 * Descriptions: wrapper of PermissionEntry
 */
public class PermissionGrantedAuthority implements GrantedAuthority {

    private PermissionEntry permissionEntry;


    public PermissionGrantedAuthority(PermissionEntry permissionEntry) {
        this.permissionEntry = permissionEntry;
    }

    @Override
    public String getAuthority() {
        return permissionEntry != null ? permissionEntry.toString() : null;
    }
}
