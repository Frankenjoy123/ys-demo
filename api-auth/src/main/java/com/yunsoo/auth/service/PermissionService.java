package com.yunsoo.auth.service;

import com.yunsoo.auth.config.AuthCacheConfig;
import com.yunsoo.auth.dao.entity.PermissionActionEntity;
import com.yunsoo.auth.dao.entity.PermissionPolicyEntity;
import com.yunsoo.auth.dao.entity.PermissionResourceEntity;
import com.yunsoo.auth.dao.repository.PermissionActionRepository;
import com.yunsoo.auth.dao.repository.PermissionPolicyRepository;
import com.yunsoo.auth.dao.repository.PermissionResourceRepository;
import com.yunsoo.auth.dto.PermissionAction;
import com.yunsoo.auth.dto.PermissionPolicy;
import com.yunsoo.auth.dto.PermissionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@AuthCacheConfig
@Service
public class PermissionService {

    @Autowired
    private PermissionResourceRepository permissionResourceRepository;

    @Autowired
    private PermissionActionRepository permissionActionRepository;

    @Autowired
    private PermissionPolicyRepository permissionPolicyRepository;


    @Cacheable(key = "'permission:resource/list'")
    public List<PermissionResource> getPermissionResources() {
        return permissionResourceRepository.findAll().stream().map(this::toPermissionResource).collect(Collectors.toList());
    }

    @Cacheable(key = "'permission:action/list'")
    public List<PermissionAction> getPermissionActions() {
        return permissionActionRepository.findAll().stream().map(this::toPermissionAction).collect(Collectors.toList());
    }

    @Cacheable(key = "'permission:policy/list'")
    public List<PermissionPolicy> getPermissionPolicies() {
        return permissionPolicyRepository.findAll().stream().map(this::toPermissionPolicy).collect(Collectors.toList());
    }


    //region private methods

    private PermissionResource toPermissionResource(PermissionResourceEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionResource permissionResource = new PermissionResource();
        permissionResource.setCode(entity.getCode());
        permissionResource.setName(entity.getName());
        permissionResource.setDescription(entity.getDescription());
        permissionResource.setActions(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getActions())));
        return permissionResource;
    }

    private PermissionAction toPermissionAction(PermissionActionEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionAction permissionAction = new PermissionAction();
        permissionAction.setCode(entity.getCode());
        permissionAction.setName(entity.getName());
        permissionAction.setDescription(entity.getDescription());
        return permissionAction;
    }

    private PermissionPolicy toPermissionPolicy(PermissionPolicyEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionPolicy permissionPolicy = new PermissionPolicy();
        permissionPolicy.setCode(entity.getCode());
        permissionPolicy.setName(entity.getName());
        permissionPolicy.setDescription(entity.getDescription());
        permissionPolicy.setPermissions(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getPermissions())));
        return permissionPolicy;
    }

    //endregion

}
