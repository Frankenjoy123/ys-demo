package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductSalesTerritoryEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   Haitao
 * Created on:   2015/8/25
 * Descriptions:
 */
public interface ProductSalesTerritoryRepository extends FindOneAndSaveRepository<ProductSalesTerritoryEntity, String> {

    List<ProductSalesTerritoryEntity> findByProductKey(String productKey);

    List<ProductSalesTerritoryEntity> findByLocationId(String locationId);


    void delete(String id);

}
