package com.yunsoo.service.contract.lookup;

import com.yunsoo.jpa.entity.ProductKeyTypeEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
public class ProductKeyType extends AbstractLookup {

    public static ProductKeyType fromEntity(ProductKeyTypeEntity entity) {
        if (entity == null) return null;
        ProductKeyType productKeyType = new ProductKeyType();
        BeanUtils.copyProperties(entity, productKeyType);
        return productKeyType;
    }

    public static ProductKeyTypeEntity toEntity(ProductKeyType productKeyType) {
        if (productKeyType == null) return null;
        ProductKeyTypeEntity entity = new ProductKeyTypeEntity();
        BeanUtils.copyProperties(productKeyType, entity);
        return entity;
    }

    public static List<ProductKeyType> fromEntityList(Iterable<ProductKeyTypeEntity> modelList) {
        if (modelList == null) return null;
        return StreamSupport.stream(modelList.spliterator(), false).map(ProductKeyType::fromEntity).collect(Collectors.toList());
    }

    public static List<ProductKeyTypeEntity> toEntityList(List<ProductKeyType> productKeyTypeList) {
        if (productKeyTypeList == null) return null;
        return productKeyTypeList.stream().map(ProductKeyType::toEntity).collect(Collectors.toList());
    }

}
