package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseTemplateEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-01-19
 * Descriptions:
 */
public interface ProductBaseTemplateRepository extends Repository<ProductBaseTemplateEntity, String> {

    ProductBaseTemplateEntity findOne(String id);

    List<ProductBaseTemplateEntity> findAll();

    List<ProductBaseTemplateEntity> findByDeletedFalse();

    List<ProductBaseTemplateEntity> findByCategoryIdOrCategoryIdIsNull(String categoryId);
}
