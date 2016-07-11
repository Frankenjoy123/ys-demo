package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.PermissionAction;
import com.yunsoo.auth.dto.PermissionEntry;
import com.yunsoo.auth.dto.PermissionPolicy;
import com.yunsoo.auth.dto.PermissionResource;
import com.yunsoo.auth.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'permission_resource:read')")
    public List<PermissionResource> getResources() {
        return permissionService.getPermissionResources();
    }

    @RequestMapping(value = "/action", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'permission_action:read')")
    public List<PermissionAction> getActions() {
        return permissionService.getPermissionActions();
    }

    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'permission_policy:read')")
    public List<PermissionPolicy> getAllPolicies() {
        return permissionService.getPermissionPolicies();
    }

}
