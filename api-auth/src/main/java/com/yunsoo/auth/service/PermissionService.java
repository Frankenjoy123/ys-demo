package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.dao.entity.PermissionActionEntity;
import com.yunsoo.auth.dao.entity.PermissionPolicyEntity;
import com.yunsoo.auth.dao.entity.PermissionRegionEntity;
import com.yunsoo.auth.dao.entity.PermissionResourceEntity;
import com.yunsoo.auth.dao.repository.PermissionActionRepository;
import com.yunsoo.auth.dao.repository.PermissionPolicyRepository;
import com.yunsoo.auth.dao.repository.PermissionRegionRepository;
import com.yunsoo.auth.dao.repository.PermissionResourceRepository;
import com.yunsoo.auth.dto.PermissionAction;
import com.yunsoo.auth.dto.PermissionPolicy;
import com.yunsoo.auth.dto.PermissionRegion;
import com.yunsoo.auth.dto.PermissionResource;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionResourceRepository permissionResourceRepository;

    @Autowired
    private PermissionActionRepository permissionActionRepository;

    @Autowired
    private PermissionRegionRepository permissionRegionRepository;

    @Autowired
    private PermissionPolicyRepository permissionPolicyRepository;


    //@Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'resourceList')")
    public List<PermissionResource> getPermissionResources() {
        return permissionResourceRepository.findAll().stream().map(this::toPermissionResource).collect(Collectors.toList());
    }

    //@Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'actionList')")
    public List<PermissionAction> getPermissionActions() {
        return permissionActionRepository.findAll().stream().map(this::toPermissionAction).collect(Collectors.toList());
    }

    //@Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'policyList')")
    public List<PermissionPolicy> getPermissionPolicies() {
        return permissionPolicyRepository.findAll().stream().map(this::toPermissionPolicy).collect(Collectors.toList());
    }

    //region region

    //@Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'region/' + #id)")
    public PermissionRegion getPermissionRegionById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return toPermissionRegion(permissionRegionRepository.findOne(id));
    }

    /**
     * put the orgRestriction(orgId) to the restrictions of default permission region of the masterOrgId
     *
     * @param masterOrgId    current org id
     * @param orgRestriction sub org id
     */
    @Transactional
    public void putOrgRestrictionToDefaultPermissionRegion(String masterOrgId, String orgRestriction) {
        orgRestriction = new RestrictionExpression.OrgRestrictionExpression(orgRestriction).toString();
        PermissionRegion defaultPR = getOrCreateDefaultPermissionRegion(masterOrgId);
        defaultPR.getRestrictions().add(orgRestriction);
        permissionRegionRepository.save(toPermissionRegionEntity(defaultPR));
    }

    @Transactional
    public PermissionRegion getOrCreateDefaultPermissionRegion(String orgId) {
        PermissionRegionEntity defaultPR;
        List<PermissionRegionEntity> defaultRegionEntities = permissionRegionRepository.findByOrgIdAndTypeCode(orgId, Constants.PermissionRegionType.DEFAULT);
        if (defaultRegionEntities.size() == 0) {
            //create new default region
            PermissionRegionEntity entity = new PermissionRegionEntity();
            entity.setOrgId(orgId);
            entity.setName("Default Region");
            entity.setRestrictions("");
            entity.setTypeCode(Constants.PermissionRegionType.DEFAULT);
            defaultPR = permissionRegionRepository.save(entity);
        } else if (defaultRegionEntities.size() == 1) {
            defaultPR = defaultRegionEntities.get(0);
        } else {
            defaultPR = defaultRegionEntities.get(0);
            //merge other default regions if exist
            List<String> restrictions = Arrays.asList(StringUtils.commaDelimitedListToStringArray(defaultPR.getRestrictions()));
            for (int i = 1; i < defaultRegionEntities.size(); i++) {
                restrictions.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(defaultRegionEntities.get(i).getRestrictions())));
            }
            defaultPR.setRestrictions(StringUtils.collectionToCommaDelimitedString(restrictions.stream().distinct().sorted().collect(Collectors.toList())));
            permissionRegionRepository.save(defaultPR);
            for (int i = 1; i < defaultRegionEntities.size(); i++) {
                permissionRegionRepository.delete(defaultRegionEntities.get(i).getId());
            }
        }
        return toPermissionRegion(defaultPR);
    }


    //endregion


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

    private PermissionRegion toPermissionRegion(PermissionRegionEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionRegion permissionRegion = new PermissionRegion();
        permissionRegion.setId(entity.getId());
        permissionRegion.setOrgId(entity.getOrgId());
        permissionRegion.setName(entity.getName());
        permissionRegion.setDescription(entity.getDescription());
        permissionRegion.setRestrictions(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getRestrictions())));
        permissionRegion.setTypeCode(entity.getTypeCode());
        return permissionRegion;
    }

    private PermissionRegionEntity toPermissionRegionEntity(PermissionRegion permissionRegion) {
        if (permissionRegion == null) {
            return null;
        }
        PermissionRegionEntity entity = new PermissionRegionEntity();
        entity.setId(permissionRegion.getId());
        entity.setOrgId(permissionRegion.getOrgId());
        entity.setName(permissionRegion.getName());
        entity.setDescription(permissionRegion.getDescription());
        String restrictions;
        if (permissionRegion.getRestrictions() != null) {
            restrictions = StringUtils.collectionToCommaDelimitedString(permissionRegion.getRestrictions().stream().distinct().sorted().collect(Collectors.toList()));
        } else {
            restrictions = "";
        }
        entity.setRestrictions(restrictions);
        entity.setTypeCode(permissionRegion.getTypeCode());
        return entity;
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
