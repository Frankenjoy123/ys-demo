package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.PermissionActionEntity;
import com.yunsoo.data.service.entity.PermissionPolicyEntity;
import com.yunsoo.data.service.entity.PermissionRegionEntity;
import com.yunsoo.data.service.entity.PermissionResourceEntity;
import com.yunsoo.data.service.repository.PermissionActionRepository;
import com.yunsoo.data.service.repository.PermissionPolicyRepository;
import com.yunsoo.data.service.repository.PermissionRegionRepository;
import com.yunsoo.data.service.repository.PermissionResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionActionRepository permissionActionRepository;

    @Autowired
    private PermissionResourceRepository permissionResourceRepository;

    @Autowired
    private PermissionRegionRepository permissionRegionRepository;

    @Autowired
    private PermissionPolicyRepository permissionPolicyRepository;


    //resource
    @RequestMapping(value = "resource", method = RequestMethod.GET)
    public List<PermissionResourceObject> getPermissionResources() {
        return permissionResourceRepository.findAll().stream().map(this::toPermissionResourceObject).collect(Collectors.toList());
    }

    //action
    @RequestMapping(value = "action", method = RequestMethod.GET)
    public List<PermissionActionObject> getPermissionActions() {
        return permissionActionRepository.findAll().stream().map(this::toPermissionActionObject).collect(Collectors.toList());
    }

    //region region

    @RequestMapping(value = "region", method = RequestMethod.GET)
    public List<PermissionRegionObject> getPermissionRegions(@RequestParam(name = "org_id", required = true) String orgId,
                                                             @RequestParam(name = "type_code", required = false) String typeCode) {
        List<PermissionRegionEntity> entities;
        if (StringUtils.isEmpty(typeCode)) {
            entities = permissionRegionRepository.findByOrgId(orgId);
        } else {
            entities = permissionRegionRepository.findByOrgIdAndTypeCode(orgId, typeCode);
        }
        return entities.stream().map(this::toPermissionRegionObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "region/{id}", method = RequestMethod.GET)
    public PermissionRegionObject getPermissionRegion(@PathVariable("id") String id) {
        PermissionRegionEntity entity = permissionRegionRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException(String.format("permissionRegion not found [id: %s]", id));
        }
        return toPermissionRegionObject(entity);
    }

    @RequestMapping(value = "region", method = RequestMethod.POST)
    public PermissionRegionObject createPermissionRegion(@RequestBody @Valid PermissionRegionObject regionObject) {
        PermissionRegionEntity entity = toPermissionRegionEntity(regionObject);
        entity.setId(null);
        return toPermissionRegionObject(permissionRegionRepository.save(entity));
    }

    @RequestMapping(value = "region/{id}", method = RequestMethod.PATCH)
    public void patchUpdatePermissionRegion(@PathVariable("id") String id,
                                            @RequestBody PermissionRegionObject regionObject) {
        PermissionRegionEntity entity = permissionRegionRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException(String.format("permissionRegion not found [id: %s]", id));
        }
        if (regionObject.getName() != null) {
            entity.setName(regionObject.getName());
        }
        if (regionObject.getDescription() != null) {
            entity.setDescription(regionObject.getDescription());
        }
        if (regionObject.getRestrictions() != null) {
            entity.setRestrictions(StringUtils.collectionToCommaDelimitedString(
                    regionObject.getRestrictions().stream().distinct().sorted().collect(Collectors.toList())));
        }
        permissionRegionRepository.save(entity);
    }

    @RequestMapping(value = "region/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePermissionRegion(@PathVariable("id") String id) {
        PermissionRegionEntity entity = permissionRegionRepository.findOne(id);
        if (entity != null) {
            permissionRegionRepository.delete(id);
        }
    }

    //endregion

    //region policy

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

    //endregion


    private PermissionResourceObject toPermissionResourceObject(PermissionResourceEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionResourceObject object = new PermissionResourceObject();
        object.setCode(entity.getCode());
        object.setName(entity.getName());
        object.setDescription(entity.getDescription());
        if (entity.getActions() != null) {
            object.setActions(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getActions())));
        }
        return object;
    }

    private PermissionActionObject toPermissionActionObject(PermissionActionEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionActionObject object = new PermissionActionObject();
        object.setCode(entity.getCode());
        object.setName(entity.getName());
        object.setDescription(entity.getDescription());
        return object;
    }

    private PermissionRegionObject toPermissionRegionObject(PermissionRegionEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionRegionObject object = new PermissionRegionObject();
        object.setId(entity.getId());
        object.setOrgId(entity.getOrgId());
        object.setName(entity.getName());
        object.setDescription(entity.getDescription());
        if (entity.getRestrictions() != null && entity.getRestrictions().length() > 0) {
            object.setRestrictions(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getRestrictions())));
        } else {
            object.setRestrictions(new ArrayList<>());
        }
        object.setTypeCode(entity.getTypeCode());
        return object;
    }

    private PermissionRegionEntity toPermissionRegionEntity(PermissionRegionObject object) {
        if (object == null) {
            return null;
        }
        PermissionRegionEntity entity = new PermissionRegionEntity();
        entity.setId(object.getId());
        entity.setOrgId(object.getOrgId());
        entity.setName(object.getName());
        entity.setDescription(object.getDescription());
        if (object.getRestrictions() != null) {
            entity.setRestrictions(StringUtils.collectionToCommaDelimitedString(
                    object.getRestrictions().stream().distinct().sorted().collect(Collectors.toList())));
        } else {
            entity.setRestrictions("");
        }
        entity.setTypeCode(object.getTypeCode());
        return entity;
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
