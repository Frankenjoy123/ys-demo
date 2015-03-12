package com.yunsoo.dao;

import com.yunsoo.dbmodel.PermissionModel;

import java.util.List;

/**
 * @author KB
 */
public interface PermissionDao {
    public PermissionModel get(long id);

    public long insert(PermissionModel permission);

    public PermissionModel get(String resource, String action);

    public List<PermissionModel> getPermissions(long groupId);

    public List<PermissionModel> getList();
}