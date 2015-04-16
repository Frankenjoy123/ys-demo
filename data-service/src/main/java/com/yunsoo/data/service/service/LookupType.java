package com.yunsoo.data.service.service;

import com.yunsoo.data.service.entity.*;
import com.yunsoo.data.service.repository.*;

/**
 * Created by:   Lijian
 * Created on:   2015/4/16
 * Descriptions:
 */
public enum LookupType {

    ProductStatus(ProductStatusEntity.class, ProductStatusRepository.class),

    ProductKeyType(ProductKeyTypeEntity.class, ProductKeyTypeRepository.class),

    ProductKeyBatchStatus(ProductKeyBatchStatusEntity.class, ProductKeyBatchStatusRepository.class),

    PermissionResource(PermissionResourceEntity.class, PermissionResourceRepository.class),

    PermissionAction(PermissionActionEntity.class, PermissionActionRepository.class);


    private final Class<? extends AbstractLookupEntity> entityType;

    private final Class<? extends LookupRepository> repositoryType;


    LookupType(Class<? extends AbstractLookupEntity> entityType, Class<? extends LookupRepository> repositoryType) {
        this.entityType = entityType;
        this.repositoryType = repositoryType;
    }


    public Class<? extends AbstractLookupEntity> getEntityType() {
        return entityType;
    }

    public Class<? extends LookupRepository> getRepositoryType() {
        return repositoryType;
    }
}
