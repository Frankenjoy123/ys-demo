package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.AccountPermission;

/**
 * @author KB
 */
public interface AccountPermissionService {
    public AccountPermission getPermissions(long orgId, long accountId);
}
