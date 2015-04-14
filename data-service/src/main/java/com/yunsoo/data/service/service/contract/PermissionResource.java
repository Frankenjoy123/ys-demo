package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.PermissionResourceEntity;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
public class PermissionResource extends AbstractLookup {

    public static PermissionResourceEntity toEntity(PermissionResource lookup) {
        return toEntity(PermissionResourceEntity.class, lookup);
    }

    public static List<PermissionResourceEntity> toEntities(List<PermissionResource> lookupList) {
        return toEntities(PermissionResourceEntity.class, lookupList);
    }

    public static PermissionResource fromEntity(PermissionResourceEntity entity) {
        return fromEntity(PermissionResource.class, entity);
    }

    public static List<PermissionResource> fromEntities(Iterable<PermissionResourceEntity> entities) {
        return fromEntities(PermissionResource.class, entities);
    }
}
