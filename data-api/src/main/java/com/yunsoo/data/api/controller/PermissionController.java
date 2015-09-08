package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.PermissionObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.PermissionPolicyEntity;
import com.yunsoo.data.service.repository.PermissionPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionPolicyRepository permissionPolicyRepository;

    @RequestMapping(value = "policy", method = RequestMethod.GET)
    public List<PermissionPolicyObject> getAllPolicies() {
        List<PermissionPolicyEntity> entities = permissionPolicyRepository.findAll();
        return toPermissionPolicyObjectList(entities);
    }

    @RequestMapping(value = "policy/{code}", method = RequestMethod.GET)
    public PermissionPolicyObject getPolicyByCode(@PathVariable(value = "code") String code) {
        List<PermissionPolicyEntity> entities = permissionPolicyRepository.findByCode(code);
        List<PermissionPolicyObject> pps = toPermissionPolicyObjectList(entities);
        if (pps == null || pps.size() == 0) {
            throw new NotFoundException("permission policy not found by [code: " + code + "]");
        }
        return pps.get(0);
    }

    @RequestMapping(value = "policy/{code}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public PermissionPolicyObject create(@PathVariable(value = "code") String code,
                                         @RequestBody @Valid PermissionPolicyObject permissionPolicyObject) {

        List<PermissionPolicyEntity> currentEntities = permissionPolicyRepository.findByCode(code);
        if (currentEntities != null && currentEntities.size() > 0) {
            throw new ConflictException("permission policy with [code: " + code + "] already exists");
        }
        permissionPolicyObject.setCode(code);
        List<PermissionPolicyEntity> entities = toPermissionPolicyEntityList(permissionPolicyObject);
        List<PermissionPolicyEntity> savedEntities = permissionPolicyRepository.save(entities);
        return toPermissionPolicyObjectList(savedEntities).get(0);
    }

    @RequestMapping(value = "policy/{code}", method = RequestMethod.PUT)
    public PermissionPolicyObject update(@PathVariable(value = "code") String code,
                                         @RequestBody @Valid PermissionPolicyObject permissionPolicyObject) {

        List<PermissionPolicyEntity> currentEntities = permissionPolicyRepository.findByCode(code);
        if (currentEntities != null && currentEntities.size() > 0) {
            permissionPolicyRepository.delete(currentEntities);
        }
        permissionPolicyObject.setCode(code);
        List<PermissionPolicyEntity> entities = toPermissionPolicyEntityList(permissionPolicyObject);
        List<PermissionPolicyEntity> savedEntities = permissionPolicyRepository.save(entities);
        return toPermissionPolicyObjectList(savedEntities).get(0);
    }

    @RequestMapping(value = "policy/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "code") String code) {
        List<PermissionPolicyEntity> currentEntities = permissionPolicyRepository.findByCode(code);
        if (currentEntities == null || currentEntities.size() == 0) {
            throw new NotFoundException("permission policy not found by [code: " + code + "]");
        }
        permissionPolicyRepository.delete(currentEntities);
    }

    private List<PermissionPolicyObject> toPermissionPolicyObjectList(List<PermissionPolicyEntity> entities) {
        if (entities == null) {
            return null;
        }
        Map<String, PermissionPolicyObject> ppMap = new HashMap<>();
        List<PermissionPolicyObject> pps = new ArrayList<>();
        entities.forEach(ppe -> {
            PermissionPolicyObject pp;
            String ppCode = ppe.getCode();
            if (ppMap.containsKey(ppCode)) {
                pp = ppMap.get(ppCode);
            } else {
                pps.add(pp = new PermissionPolicyObject());
                pp.setCode(ppCode);
                pp.setName(ppe.getName());
                pp.setDescription(ppe.getDescription());
                pp.setPermissions(new ArrayList<>());
                ppMap.put(ppCode, pp);
            }
            PermissionObject p = new PermissionObject();
            p.setResourceCode(ppe.getResourceCode());
            p.setActionCode(ppe.getActionCode());
            pp.getPermissions().add(p);
        });
        return pps;
    }

    private List<PermissionPolicyEntity> toPermissionPolicyEntityList(PermissionPolicyObject permissionPolicyObject) {
        if (permissionPolicyObject == null || permissionPolicyObject.getPermissions() == null) {
            return null;
        }
        List<PermissionObject> permissions = permissionPolicyObject.getPermissions();
        List<PermissionPolicyEntity> entities = new ArrayList<>();
        String code = permissionPolicyObject.getCode();
        String name = permissionPolicyObject.getName();
        String desc = permissionPolicyObject.getDescription();
        permissions.forEach(p -> {
            PermissionPolicyEntity entity = new PermissionPolicyEntity();
            entity.setCode(code);
            entity.setName(name);
            entity.setDescription(desc);
            entity.setResourceCode(p.getResourceCode());
            entity.setActionCode(p.getActionCode());
            entities.add(entity);
        });
        return entities;
    }
}
