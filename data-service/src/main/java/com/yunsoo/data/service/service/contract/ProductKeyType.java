package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.ProductKeyTypeEntity;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
public class ProductKeyType extends AbstractLookup {

    public static ProductKeyTypeEntity toEntity(ProductKeyType lookup) {
        return toEntity(ProductKeyTypeEntity.class, lookup);
    }

    public static List<ProductKeyTypeEntity> toEntities(List<ProductKeyType> lookupList) {
        return toEntities(ProductKeyTypeEntity.class, lookupList);
    }

    public static ProductKeyType fromEntity(ProductKeyTypeEntity entity) {
        return fromEntity(ProductKeyType.class, entity);
    }

    public static List<ProductKeyType> fromEntities(Iterable<ProductKeyTypeEntity> entities) {
        return fromEntities(ProductKeyType.class, entities);
    }
}
