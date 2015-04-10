package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.Permission;

import java.util.List;

/**
 * @author KB
 */
public interface PermissionService {
    public List<Permission> getPermissions();
    public Permission getPermission(String resource, String action);
}
