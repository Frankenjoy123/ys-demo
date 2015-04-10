package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.ProductKeyBatchStatusEntity;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/24
 * Descriptions:
 */
public class ProductKeyBatchStatus extends AbstractLookup {

    public static ProductKeyBatchStatusEntity toEntity(ProductKeyBatchStatus lookup) {
        return toEntity(ProductKeyBatchStatusEntity.class, lookup);
    }

    public static List<ProductKeyBatchStatusEntity> toEntities(List<ProductKeyBatchStatus> lookupList) {
        return toEntities(ProductKeyBatchStatusEntity.class, lookupList);
    }

    public static ProductKeyBatchStatus fromEntity(ProductKeyBatchStatusEntity entity) {
        return fromEntity(ProductKeyBatchStatus.class, entity);
    }

    public static List<ProductKeyBatchStatus> fromEntities(Iterable<ProductKeyBatchStatusEntity> entities) {
        return fromEntities(ProductKeyBatchStatus.class, entities);
    }
}
