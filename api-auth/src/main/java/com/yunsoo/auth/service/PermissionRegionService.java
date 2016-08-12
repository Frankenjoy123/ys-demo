package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.dao.entity.PermissionRegionEntity;
import com.yunsoo.auth.dao.repository.PermissionRegionRepository;
import com.yunsoo.auth.dto.PermissionRegion;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-29
 * Descriptions:
 */
@Service
public class PermissionRegionService {

    @Autowired
    private PermissionRegionRepository permissionRegionRepository;


    public PermissionRegion getById(String regionId) {
        if (StringUtils.isEmpty(regionId)) {
            return null;
        }
        return toPermissionRegion(permissionRegionRepository.findOne(regionId));
    }

    public List<PermissionRegion> getList(String orgId, String typeCode) {
        if (StringUtils.isEmpty(orgId)) {
            return new ArrayList<>();
        }
        List<PermissionRegionEntity> entities = typeCode == null
                ? permissionRegionRepository.findByOrgId(orgId)
                : permissionRegionRepository.findByOrgIdAndTypeCode(orgId, typeCode);

        return entities.stream().map(this::toPermissionRegion).collect(Collectors.toList());
    }

    public PermissionRegion create(PermissionRegion permissionRegion) {
        PermissionRegionEntity entity = toPermissionRegionEntity(permissionRegion);
        entity.setId(null);
        entity.setTypeCode(Constants.PermissionRegionType.CUSTOM);
        return toPermissionRegion(permissionRegionRepository.save(entity));
    }

    @Transactional
    public List<String> addRestrictionsToRegionById(String regionId, List<String> restrictions) {
        if (StringUtils.isEmpty(regionId)) {
            return new ArrayList<>();
        }
        PermissionRegionEntity entity = permissionRegionRepository.findOne(regionId);
        if (entity == null) {
            return new ArrayList<>();
        }
        List<String> currentRss = toList(entity.getRestrictions());
        List<String> newRss = toList(RestrictionExpression.parse(toString(restrictions)).toString());
        if (newRss.size() == 0) {
            return currentRss;
        }
        currentRss.addAll(newRss);
        entity.setRestrictions(toString(currentRss));
        entity = permissionRegionRepository.save(entity);
        return toList(entity.getRestrictions());
    }

    @Transactional
    public void patchUpdate(PermissionRegion permissionRegion) {
        PermissionRegionEntity entity = permissionRegionRepository.findOne(permissionRegion.getId());
        if (entity != null) {
            if (permissionRegion.getName() != null) entity.setName(permissionRegion.getName());
            if (permissionRegion.getDescription() != null) entity.setDescription(permissionRegion.getDescription());
            if (permissionRegion.getRestrictions() != null)
                entity.setRestrictions(toString(permissionRegion.getRestrictions()));
            permissionRegionRepository.save(entity);
        }
    }

    @Transactional
    public void deleteById(String regionId) {
        if (StringUtils.isEmpty(regionId)) {
            return;
        }
        PermissionRegionEntity entity = permissionRegionRepository.findOne(regionId);
        if (entity != null) {
            if (Constants.PermissionRegionType.DEFAULT.equals(entity.getTypeCode())) {
                throw new UnprocessableEntityException("default region can not be deleted");
            }
            permissionRegionRepository.delete(entity);
        }
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
            List<String> restrictions = toList(defaultPR.getRestrictions());
            for (int i = 1; i < defaultRegionEntities.size(); i++) {
                restrictions.addAll(toList(defaultRegionEntities.get(i).getRestrictions()));
            }
            defaultPR.setRestrictions(toString(restrictions));
            permissionRegionRepository.save(defaultPR);
            for (int i = 1; i < defaultRegionEntities.size(); i++) {
                permissionRegionRepository.delete(defaultRegionEntities.get(i));
            }
        }
        return toPermissionRegion(defaultPR);
    }


    //region private methods

    private PermissionRegion toPermissionRegion(PermissionRegionEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionRegion permissionRegion = new PermissionRegion();
        permissionRegion.setId(entity.getId());
        permissionRegion.setOrgId(entity.getOrgId());
        permissionRegion.setName(entity.getName());
        permissionRegion.setDescription(entity.getDescription());
        permissionRegion.setRestrictions(toList(entity.getRestrictions()));
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
        entity.setRestrictions(toString(permissionRegion.getRestrictions()));
        entity.setTypeCode(permissionRegion.getTypeCode());
        return entity;
    }

    private List<String> toList(String restrictions) {
        return new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(restrictions)));
    }

    private String toString(List<String> restrictions) {
        return restrictions == null ? "" : StringUtils.collectionToCommaDelimitedString(restrictions.stream().distinct().sorted().collect(Collectors.toList()));
    }

    //endregion

}
