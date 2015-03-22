package com.yunsoo.dao;

import com.yunsoo.dbmodel.AccountPermissionModel;

import java.util.List;

/**
 * @author KB
 */
public interface AccountPermissionDao {
    public AccountPermissionModel get(long orgId, long accountId);
}