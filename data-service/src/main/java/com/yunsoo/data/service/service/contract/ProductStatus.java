package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.ProductStatusEntity;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/24
 * Descriptions:
 */
public class ProductStatus extends AbstractLookup {

    public static ProductStatusEntity toEntity(ProductStatus lookup) {
        return toEntity(ProductStatusEntity.class, lookup);
    }

    public static List<ProductStatusEntity> toEntities(List<ProductStatus> lookupList) {
        return toEntities(ProductStatusEntity.class, lookupList);
    }

    public static ProductStatus fromEntity(ProductStatusEntity entity) {
        return fromEntity(ProductStatus.class, entity);
    }

    public static List<ProductStatus> fromEntities(Iterable<ProductStatusEntity> entities) {
        return fromEntities(ProductStatus.class, entities);
    }
}
