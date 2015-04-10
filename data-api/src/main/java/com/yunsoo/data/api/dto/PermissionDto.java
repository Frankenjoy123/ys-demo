package com.yunsoo.data.api.dto;

import com.yunsoo.data.service.service.contract.Permission;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/3/3.
 */
public class PermissionDto {

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

    public static Permission ToPermission(PermissionDto dto) {
        if (dto == null) return  null;
        Permission permission = new Permission();
        BeanUtils.copyProperties(dto, permission, SpringBeanUtil.getNullPropertyNames(dto));
        return permission;
    }

    public static PermissionDto FromPermission(Permission permission) {
        if (permission == null) return null;
        PermissionDto dto = new PermissionDto();
        BeanUtils.copyProperties(permission, dto, SpringBeanUtil.getNullPropertyNames(permission));
        return dto;
    }

    public static List<Permission> ToPermissionList(List<PermissionDto> dtos) {
        if (dtos == null) return null;
        List<Permission> permissions = new ArrayList<Permission>();
        for (PermissionDto dto : dtos) {
            permissions.add(PermissionDto.ToPermission(dto));
        }
        return permissions;
    }

    public static List<PermissionDto> FromPermissionList(List<Permission> permissions) {
        if (permissions == null) return null;
        List<PermissionDto> dtos = new ArrayList<PermissionDto>();
        for (Permission permission : permissions) {
            dtos.add(PermissionDto.FromPermission(permission));
        }
        return  dtos;
    }
}
