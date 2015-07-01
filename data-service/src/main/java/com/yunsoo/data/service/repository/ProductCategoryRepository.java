package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductCategoryEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
public interface ProductCategoryRepository extends FindOneAndSaveRepository<ProductCategoryEntity, Integer> {


    List<ProductCategoryEntity> findAll();

    List<ProductCategoryEntity> findByParentId(Integer parentId);
}
