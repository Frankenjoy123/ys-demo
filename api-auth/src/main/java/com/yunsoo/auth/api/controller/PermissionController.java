package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.*;
import com.yunsoo.auth.service.PermissionService;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * get all permissions of the current account
     *
     * @return all permissions
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PermissionEntry> getPermissions() {
        return AuthUtils.getAuthentication().getPermissionEntries().stream().map(PermissionEntry::new).collect(Collectors.toList());
    }

    /**
     * check the current account if has the giving permission
     *
     * @param request restriction and permission
     * @return boolean if has permission
     */
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public boolean checkPermission(@RequestBody PermissionCheckRequest request) {
        RestrictionExpression restriction = RestrictionExpression.parse(request.getRestriction());
        PermissionExpression permission = PermissionExpression.parse(request.getPermission());
        return AuthUtils.getAuthentication().checkPermission(restriction, permission);
    }

    @RequestMapping(value = "resource", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'permission_resource:read')")
    public List<PermissionResource> getResources() {
        return permissionService.getPermissionResources();
    }

    @RequestMapping(value = "action", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'permission_action:read')")
    public List<PermissionAction> getActions() {
        return permissionService.getPermissionActions();
    }

    @RequestMapping(value = "policy", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'permission_policy:read')")
    public List<PermissionPolicy> getAllPolicies() {
        return permissionService.getPermissionPolicies();
    }

}
