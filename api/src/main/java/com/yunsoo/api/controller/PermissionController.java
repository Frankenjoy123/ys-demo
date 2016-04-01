package com.yunsoo.api.controller;

import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.dto.PermissionAction;
import com.yunsoo.api.dto.PermissionEntry;
import com.yunsoo.api.dto.PermissionPolicy;
import com.yunsoo.api.dto.PermissionResource;
import com.yunsoo.api.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private PermissionDomain permissionDomain;

    /**
     * get all permissions of the current account
     *
     * @return all permissions
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PermissionEntry> getPermissions() {
        return AuthUtils.getAuthentication().getPermissionEntries().stream().map(p -> {
            PermissionEntry pe = new PermissionEntry();
            pe.setId(p.getId());
            pe.setPrincipal(p.getPrincipal().toString());
            pe.setRestriction(p.getRestriction().toString());
            pe.setPermission(p.getPermission().toString());
            pe.setEffect(p.getEffect().name());
            return pe;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public List<PermissionResource> getResources() {
        Map<String, PermissionAction> actionMap = new HashMap<>();
        permissionDomain.getPermissionActions().forEach(a -> {
            actionMap.put(a.getCode(), new PermissionAction(a));
        });
        return permissionDomain.getPermissionResources().stream().map(r -> new PermissionResource(r, actionMap)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/action", method = RequestMethod.GET)
    public List<PermissionAction> getActions() {
        return permissionDomain.getPermissionActions().stream().map(PermissionAction::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    public List<PermissionPolicy> getAllPolicies() {
        return permissionDomain.getPermissionPolicies().stream().map(PermissionPolicy::new).collect(Collectors.toList());
    }

//    @RequestMapping(value = "/policy/page/enterprise", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(null, 'permissionpolicy:read')")
//    public List<PermissionPolicy> getPoliciesForPageEnterprise() {
//        return permissionDomain.getPermissionPolicies().stream()
//                .filter(p -> p.getCode().startsWith("page-enterprise-"))
//                .map(PermissionPolicy::new)
//                .collect(Collectors.toList());
//    }

}
