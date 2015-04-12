package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.dao.PermissionDao;
import com.yunsoo.data.service.service.PermissionService;
import com.yunsoo.data.service.service.contract.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author KB
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<Permission> getPermissions() {
        return Permission.FromModelList(permissionDao.getList());
    }

    @Override
    public Permission getPermission(String resource, String action) {
        return Permission.FromModel(permissionDao.get(resource, action));
    }
}