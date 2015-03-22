package com.yunsoo.service;

import com.yunsoo.service.contract.AccountPermission;

import java.util.List;

/**
 * @author KB
 */
public interface AccountPermissionService {
    public AccountPermission getPermissions(long orgId, long accountId);
}
