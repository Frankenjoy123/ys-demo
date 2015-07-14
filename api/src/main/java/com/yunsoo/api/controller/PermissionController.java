package com.yunsoo.api.controller;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.dto.PermissionAction;
import com.yunsoo.api.dto.PermissionPolicy;
import com.yunsoo.api.dto.PermissionResource;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private PermissionDomain permissionDomain;


    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null, 'permissionpolicy:read')")
    public List<PermissionPolicy> getAllPolicies() {
        return permissionDomain.getPermissionPolicies().stream().map(PermissionPolicy::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/policy/{code}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null, 'permissionpolicy:read')")
    public PermissionPolicy getPolicyByCode(@PathVariable(value = "code") String code) {
        //todo: change it getting from cached list permissionDomain.getPermissionPolicies()
        try {
            PermissionPolicyObject permissionPolicyObject = dataAPIClient.get("permission/policy/{code}", PermissionPolicyObject.class, code);
            return new PermissionPolicy(permissionPolicyObject);
        } catch (NotFoundException ignored) {
            throw new NotFoundException("permission policy not found by [code: " + code + "]");
        }
    }

    @RequestMapping(value = "/policy/page/enterprise", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null, 'permissionpolicy:read')")
    public List<PermissionPolicy> getPoliciesForPageEnterprise() {
        return permissionDomain.getPermissionPolicies().stream()
                .filter(p -> p.getCode().startsWith("page-enterprise-"))
                .map(PermissionPolicy::new)
                .collect(Collectors.toList());
    }


    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public List<PermissionResource> getResources(@RequestParam(value = "all", required = false) Boolean all) {
        return permissionDomain.getPermissionResources(all != null && all ? null : true);
    }

    @RequestMapping(value = "/action", method = RequestMethod.GET)
    public List<PermissionAction> getActions(@RequestParam(value = "all", required = false) Boolean all) {
        return permissionDomain.getPermissionActions(all != null && all ? null : true);
    }

}
