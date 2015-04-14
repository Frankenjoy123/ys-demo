package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.PermissionActionEntity;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
public class PermissionAction extends AbstractLookup {

    public static PermissionActionEntity toEntity(PermissionAction lookup) {
        return toEntity(PermissionActionEntity.class, lookup);
    }

    public static List<PermissionActionEntity> toEntities(List<PermissionAction> lookupList) {
        return toEntities(PermissionActionEntity.class, lookupList);
    }

    public static PermissionAction fromEntity(PermissionActionEntity entity) {
        return fromEntity(PermissionAction.class, entity);
    }

    public static List<PermissionAction> fromEntities(Iterable<PermissionActionEntity> entities) {
        return fromEntities(PermissionAction.class, entities);
    }
}
