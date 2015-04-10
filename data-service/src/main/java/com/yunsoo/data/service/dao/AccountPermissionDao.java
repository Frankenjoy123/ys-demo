package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.AccountPermissionModel;

/**
 * @author KB
 */
public interface AccountPermissionDao {
    public AccountPermissionModel get(long orgId, long accountId);
}