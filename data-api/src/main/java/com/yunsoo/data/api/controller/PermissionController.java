package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.PermissionActionObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.data.object.PermissionRegionObject;
import com.yunsoo.common.data.object.PermissionResourceObject;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        return entities.stream().map(this::toPermissionPolicyObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "policy/{code}", method = RequestMethod.GET)
    public PermissionPolicyObject getPolicyByCode(@PathVariable(value = "code") String code) {
        PermissionPolicyEntity entity = permissionPolicyRepository.findOne(code);
        if (entity == null) {
            throw new NotFoundException("permission policy not found by [code: " + code + "]");
        }
        return toPermissionPolicyObject(entity);
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
        List<String> actions;
        if (entity.getActions() != null) {
            actions = Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getActions()));
        } else {
            actions = new ArrayList<>();
        }
        object.setActions(actions);
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
        List<String> restrictions;
        if (entity.getRestrictions() != null && entity.getRestrictions().length() > 0) {
            restrictions = Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getRestrictions()));
        } else {
            restrictions = new ArrayList<>();
        }
        object.setRestrictions(restrictions);
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
        String restrictions;
        if (object.getRestrictions() != null) {
            restrictions = StringUtils.collectionToCommaDelimitedString(object.getRestrictions().stream().distinct().sorted().collect(Collectors.toList()));
        } else {
            restrictions = "";
        }
        entity.setRestrictions(restrictions);
        entity.setTypeCode(object.getTypeCode());
        return entity;
    }

    private PermissionPolicyObject toPermissionPolicyObject(PermissionPolicyEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionPolicyObject object = new PermissionPolicyObject();
        object.setCode(entity.getCode());
        object.setName(entity.getName());
        object.setDescription(entity.getDescription());
        List<String> permissions;
        if (entity.getPermissions() != null && entity.getPermissions().length() > 0) {
            permissions = Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getPermissions()));
        } else {
            permissions = new ArrayList<>();
        }
        object.setPermissions(permissions);
        return object;
    }

}
