package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.PermissionModel;

import java.util.List;

/**
 * @author KB
 */
public interface PermissionDao {
    public List<PermissionModel> getList();
    public PermissionModel get(String resource, String action);

    public PermissionModel get(long id);

    public long insert(PermissionModel permission);

    public List<PermissionModel> getPermissions(long groupId);

}