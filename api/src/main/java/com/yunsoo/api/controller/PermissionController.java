package com.yunsoo.api.controller;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.api.dto.Permission;
import com.yunsoo.api.dto.PermissionPolicy;
import com.yunsoo.common.data.object.PermissionObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/16
 * Descriptions:
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private DataAPIClient dataAPIClient;

    @RequestMapping(value = "policy", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null, 'permissionpolicy:read')")
    public List<PermissionPolicy> getAllPolicies() {
        List<PermissionPolicyObject> permissionPolicyObjects = dataAPIClient.get("permission/policy",
                new ParameterizedTypeReference<List<PermissionPolicyObject>>() {
                });
        return permissionPolicyObjects.stream().map(this::toPermissionPolicy).collect(Collectors.toList());
    }

    @RequestMapping(value = "policy/{code}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null, 'permissionpolicy:read')")
    public PermissionPolicy getPolicyByCode(@PathVariable(value = "code") String code) {
        PermissionPolicyObject permissionPolicyObject = dataAPIClient.get("permission/policy/{code}", PermissionPolicyObject.class, code);
        return toPermissionPolicy(permissionPolicyObject);
    }

    private PermissionPolicy toPermissionPolicy(PermissionPolicyObject object) {
        if (object == null) {
            return null;
        }
        PermissionPolicy permissionPolicy = new PermissionPolicy();
        permissionPolicy.setCode(object.getCode());
        permissionPolicy.setName(object.getName());
        permissionPolicy.setDescription(object.getDescription());
        permissionPolicy.setPermissions(toPermissionList(object.getPermissions()));
        return permissionPolicy;
    }

    private Permission toPermission(PermissionObject permissionObject) {
        if (permissionObject == null) {
            return null;
        }
        Permission permission = new Permission();
        permission.setResourceCode(permissionObject.getResourceCode());
        permission.setActionCode(permissionObject.getActionCode());
        return permission;
    }

    private List<Permission> toPermissionList(List<PermissionObject> permissionObjects) {
        if (permissionObjects == null || permissionObjects.size() == 0) {
            return new ArrayList<>(0);
        }
        return permissionObjects.stream().map(this::toPermission).collect(Collectors.toList());
    }

}
