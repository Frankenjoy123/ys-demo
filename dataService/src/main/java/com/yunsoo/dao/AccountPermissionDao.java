package com.yunsoo.dao;

import com.yunsoo.dbmodel.AccountPermissionModel;

import java.util.List;

/**
 * @author KB
 */
public interface AccountPermissionDao {
    public AccountPermissionModel getByAccountOrg(long accountOrgId);
}