package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.dbmodel.PermissionModel;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KB
 */
public class Permission {
    private long id;
    private long groupId;
    private long order;
    private String resource;
    private String action;
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getGroupId() { return groupId; }
    public void setGroupId(long groupId) { this.groupId = groupId; }
    public long getOrder() { return order; }
    public void setOrder(long order) { this.order = order; }
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public static PermissionModel ToModel(Permission permission) {
        if (permission == null) return  null;
        PermissionModel model = new PermissionModel();
        BeanUtils.copyProperties(permission, model, SpringBeanUtil.getNullPropertyNames(permission));
        return model;
    }

    public static Permission FromModel(PermissionModel model) {
        if (model == null) return null;
        Permission permission = new Permission();
        BeanUtils.copyProperties(model, permission, SpringBeanUtil.getNullPropertyNames(model));
        return permission;
    }

    public static List<Permission> FromModelList(List<PermissionModel> models) {
        if (models == null) return null;
        List<Permission> permissions = new ArrayList<Permission>();
        for (PermissionModel model : models) {
            permissions.add(Permission.FromModel(model));
        }
        return permissions;
    }

    public static List<PermissionModel> ToModelList(List<Permission> permissions) {
        if (permissions == null) return null;
        List<PermissionModel> models = new ArrayList<PermissionModel>();
        for (Permission permission : permissions) {
            models.add(Permission.ToModel(permission));
        }
        return  models;
    }
}
