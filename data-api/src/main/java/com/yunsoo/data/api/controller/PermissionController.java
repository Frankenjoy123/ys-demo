package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.data.object.PermissionObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.PermissionPolicyEntity;
import com.yunsoo.data.service.entity.PermissionResourceEntity;
import com.yunsoo.data.service.repository.PermissionPolicyRepository;
import com.yunsoo.data.service.repository.PermissionResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionResourceRepository permissionResourceRepository;

    @Autowired
    private PermissionPolicyRepository permissionPolicyRepository;

    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    public List<PermissionPolicyObject> getAllPolicies() {
        Map<String, PermissionPolicyObject> ppMap = new HashMap<>();
        List<PermissionPolicyObject> pps = new ArrayList<>();
        permissionPolicyRepository.findAll().forEach(ppe -> {
            PermissionPolicyObject pp;
            String ppCode = ppe.getCode();
            if (ppMap.containsKey(ppCode)) {
                pp = ppMap.get(ppCode);
            } else {
                pps.add(pp = new PermissionPolicyObject());
                ppMap.put(ppCode, pp);
                pp.setPolicyCode(ppCode);
                pp.setPolicyName(ppe.getName());
                pp.setDescription(ppe.getDescription());
                pp.setPermissions(new ArrayList<>());
            }
            PermissionObject p = new PermissionObject();
            p.setResourceCode(ppe.getResourceCode());
            p.setActionCode(ppe.getActionCode());
            pp.getPermissions().add(p);
        });
        return pps;
    }

    @RequestMapping(value = "/policy/{code}", method = RequestMethod.GET)
    public PermissionPolicyObject getPolicyByCode(@PathVariable(value = "code") String code) {
        PermissionPolicyObject pp = null;
        for (PermissionPolicyEntity ppe : permissionPolicyRepository.findByCode(code)) {
            if (pp == null) {
                pp = new PermissionPolicyObject();
                pp.setPolicyCode(ppe.getCode());
                pp.setPolicyName(ppe.getName());
                pp.setDescription(ppe.getDescription());
                pp.setPermissions(new ArrayList<>());
            }
            PermissionObject p = new PermissionObject();
            p.setResourceCode(ppe.getResourceCode());
            p.setActionCode(ppe.getActionCode());
            pp.getPermissions().add(p);
        }
        if (pp == null) {
            throw new NotFoundException("permission policy not found by [code: " + code + "]");
        }
        return pp;
    }

    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public List<PermissionResourceEntity> getAllResource(@RequestParam(value = "active", required = false) Boolean active) {
        return StreamSupport.stream(permissionResourceRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/action", method = RequestMethod.GET)
    public List<LookupObject> getAllAction(@RequestParam(value = "active", required = false) Boolean active) {
        return null;
    }
}
